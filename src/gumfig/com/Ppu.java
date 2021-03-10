package gumfig.com;

public class Ppu {
    // Variables
    Nes nes;
    // 2-1KB area of memory used by the PPU to lay out backgrounds.
    // This could be assigned to 4kb though.
    private int[][] nameTable, sprPatternTable;
    // Palette data: $3F00 -> $3F1F
    // 64 bytes representing colors
    private final int[] paletteTable;
    // Internal mem in PPU that contains a display list of up to 64 sprites.
    private oamEntry[] OAM;
    // $2003 OAMADDR initially set to $00
    private int oamAddr;
    // Misc. variables using in rendering
    private int scanline, cycles, buffer;
    private BackgroundShifter shifter;
    // Pointer address into nametable to get background tile info
    private AddressRegister vramAddr, tmpAddr;
    // Or just fineX
    private int xOffset;
    //Mask and Control registers
    public Mask mask;
    public Control control;
    //First write (address latch)
    private boolean oddFrame, firstWrite;
    // Current frame to be drawn
    public int[] curFrame;
    public boolean complete;

    // The 4 bytes assigned to a single Object in the OAM
    static class oamEntry {
        public int x, y; // Sprite X and Y pos
        public int attribute; // Flags define how a sprite is rendered. Flipped Horizontally/Vertically
        public int id; // Tile index of sprite within pattern table
    }

    // PPU Registers
    // $2000 PPUCTRL
    // 8 bit long register
    public static class Control {
        // Base nametable address (0 = $2000; 1 = $2400; 2 = $2800; 3 = $2C00)
        // Equivalently, bits 1 and 0 are the most significant bit of the scrolling
        // coordinates (see Nametables and PPUSCROLL):
        boolean nametableX, nametableY, Increment, spritePattern, backgroundPattern, spriteSize, Slave, enableNMI;
        public int reg;

        Control() {
            setRegister(0);
        }

        public void setRegister(int val) {
            // Store the bit states in vars so i dont have to call a function everytime
            this.reg = val;
            this.nametableX = (val & (1)) > 0;
            this.nametableY = (val & (1 << 1)) > 0;
            this.Increment = (val & (1 << 2)) > 0;
            this.spritePattern = (val & (1 << 3)) > 0;
            this.backgroundPattern = (val & (1 << 4)) > 0;
            this.spriteSize = (val & (1 << 5)) > 0;
            this.Slave = (val & (1 << 6)) > 0;
            this.enableNMI = (val & (1 << 7)) > 0;
        }
    }

    // $2001 PPUMASK
    // 8 bit long register
    public static class Mask {
        boolean grayScale, backgroundLeft, spriteLeft, Background, Sprite, Red, Green, Blue;
        public int reg;

        Mask() {
            setRegister(0);
        }

        public void setRegister(int val) {
            // Store the bit states in vars so i dont have to call a function everytime
            this.reg = val;
            this.grayScale = (val & (1)) > 0;
            this.backgroundLeft = (val & (1 << 1)) > 0;
            this.spriteLeft = (val & (1 << 2)) > 0;
            this.Background = (val & (1 << 3)) > 0;
            this.Sprite = (val & (1 << 4)) > 0;
            this.Red = (val & (1 << 5)) > 0;
            this.Green = (val & (1 << 6)) > 0;
            this.Blue = (val & (1 << 7)) > 0;
        }
    }

    static class BackgroundShifter {
        // Pattern Low and High bytes
        int patternLow = 0, patternHigh = 0;
        // Atribute Low and High bytes
        int attrLow = 0, attrHigh = 0;
        // Next tile
        int nextID = 0, nextAtrr = 0, nextLow = 0, nextHigh = 0;
    }

    // $2002 PPUSTATUS
    private enum Status {
        UNUSED(1 << 4), // Bit 0 -> 4 = Insignificant
        SPROVERFLOW(1 << 5), // Bit 5 = Sprite Overflow
        SPR0HIT(1 << 6), // Bit 6 = Sprite 0 Hit
        VBLANK(1 << 7); // Bit 7 = Vertical Blank

        private final int bit;

        Status(int bit) {
            this.bit = bit;
        }
    }

    // Current VRAM address and temporary VRAM address (15 bits)
    static class AddressRegister {
        int coarseX = 0, coarseY = 0, fineY = 0;
        // Nametable Select
        boolean nametableX = false, nametableY = false;
        int reg;

        void setAddressRegister(int val) {
            // Got this code from one lone coders implementation of the Nes
            this.reg = val;
            // CoarseX and CoraseY is 5 bits long
            // fineY is only 3 bits long
            coarseX = val & 0x1F; // 0 - 4
            coarseY = (val >> 5) & 0x1F; // 5 - 9
            nametableX = (val & (1 << 10)) > 1; // 10
            nametableY = (val & (1 << 11)) > 1; // 11
            fineY = (val >> 11) & 0x7; // 12 - 14
        }
    }

    //-----------------------------------------------------------//

    // Connect to Nes
    Ppu(Nes Nes) {
        // NTSC Palette
        paletteTable = new int[]{0x525252, 0xB40000, 0xA00000, 0xB1003D, 0x740069, 0x00005B, 0x00005F, 0x001840,
                0x002F10, 0x084A08, 0x006700, 0x124200, 0x6D2800, 0, 0, 0, 0xC4D5E7, 0xFF4000, 0xDC0E22, 0xFF476B,
                0xD7009F, 0x680AD7, 0x0019BC, 0x0054B1, 0x006A5B, 0x008C03, 0x00AB00, 0x2C8800, 0xA47200, 0, 0, 0,
                0xF8F8F8, 0xFFAB3C, 0xFF7981, 0xFF5BC5, 0xFF48F2, 0xDF49FF, 0x476DFF, 0x00B4F7, 0x00E0FF, 0x00E375,
                0x03F42B, 0x78B82E, 0xE5E218, 0x787878, 0, 0, 0xFFFFFF, 0xFFF2BE, 0xF8B8B8, 0xF8B8D8, 0xFFB6FF,
                0xFFC3FF, 0xC7D1FF, 0x9ADAFF, 0x88EDF8, 0x83FFDD, 0xB8F8B8, 0xF5F8AC, 0xFFFFB0, 0xF8D8F8, 0, 0};
        this.nes = Nes;
    }

    // Init
    public void reset() {
        nameTable = new int[2][1024];
        sprPatternTable = new int[128][128];

        OAM = new oamEntry[64];
        oamAddr = 0;

        cycles = 0;
        scanline = -1;
        oddFrame = false;

        shifter = new BackgroundShifter();

        mask = new Mask();
        control = new Control();

        vramAddr = new AddressRegister();
        tmpAddr = new AddressRegister();

        curFrame = new int[256 * 240];

        buffer = 0;
    }

    // This entire clock function I yoinked fromm olc
    private void updateBackgroundShifters() {
        shifter.patternLow &= (0xFF00 | shifter.nextLow);
        shifter.patternHigh &= (0xFF00 | shifter.nextHigh);

        // Attribute bits change every 8 pixels and are synchronised with the pattern
        // shifters.
        shifter.attrLow &= 0xFF00 | ((shifter.nextAtrr & 1) > 0 ? 0xFF : 0);
        shifter.attrHigh &= 0xFF00 | ((shifter.nextAtrr & 2) > 0 ? 0xFF : 0);
    }



    public void clearFrame(){
        curFrame = new int[256 * 240];
    }

    public void setOamAddr(int data) {
        oamAddr = data;
    }

    public void updateSpritePatternTable(int index, int palette) {
        // The pattern table is divided into two 256-tile sections 16x16
        // Each tile in the pattern table is 16 bytes which are separated to left and right planes
        // Each plane is 8x8 bits
        // The first plane controls bit 0 of the color. 
        // the second plane controls bit 1. Any pixel whose color is 0 is background/transparent 
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                // X here represents a tile and Y represents the 256-tile section
                int offset = (x * 16) + (y * 256);

                for (int i = 0; i < 8; i++) {
                    //Get the first plane
                    int low = readVRAM((index * 0x1000) + i + offset);
                    //Get the second plane
                    int high = readVRAM((index * 0x1000) + (i + 0x8) + offset);

                    for (int j = 0; j < 8; j++) {
                        int point = low + high;
                        low >>= 1;
                        high >>= 1;
                        sprPatternTable[x * 8][y * 8] = getColorPalette(palette, point);
                    }

                }
            }
        }
    }

    public void writeVRAM(int addr, int data){
        addr &= 0x3FFF;
        if(addr <= 0x1FFF)
            nes.mapper.writeVROM(addr, data);
        else if(addr <= 0x3EFF){
            addr &= 0x0FFF;
            switch (nes.rom.getMirror()) {
                // Vertical Scrolling
                case VERTICAL:
                    if (addr <= 0x03FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x07FF) nameTable[1][addr & 0x03FF] = data;
                    else if (addr <= 0x0BFF) nameTable[0][addr & 0x03FF] = data;
                    else nameTable[1][addr & 0x03FF] = data;
                    // Horizontal Scrolling
                case HORIZONTAL:
                    if (addr <= 0x03FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x07FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x0BFF) nameTable[1][addr & 0x03FF] = data;
                    else nameTable[1][addr & 0x03FF] = data;
                case FOUR_SCREEN:
                    break;
                case SINGLE:
                    if (addr <= 0x03FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x07FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x0BFF) nameTable[0][addr & 0x03FF] = data;
                    else nameTable[0][addr & 0x03FF] = data;
            }
        }

    }

    public int getBuffer(){
        int tmp = buffer;
        buffer = readVRAM(vramAddr.reg);
        if(vramAddr.reg >= 0x3F00) tmp = buffer;
        vramAddr.setAddressRegister(vramAddr.reg + (control.Increment ? 32 : 1));
        return tmp;
    }

    public void scrollWrite(int data){
        if(!firstWrite){
            // First write to register contains Y offset
            tmpAddr.fineY = data & 0x7;
            tmpAddr.coarseX = data >> 3;
            firstWrite = true;
        }else{
            // Second write to register contains X offset
            xOffset = data & 0x7;
            tmpAddr.coarseX = data >> 3;
            firstWrite = false;
        }
    }

    public void addressWrite(int data){
        if(!firstWrite){
            tmpAddr.setAddressRegister(((data & 0x3F) << 8) | (tmpAddr.reg & 0xFF));
            firstWrite = true;
        }else{
            tmpAddr.setAddressRegister((tmpAddr.reg & 0xFF00) | data);
            vramAddr.setAddressRegister(tmpAddr.reg);
            firstWrite = false;
        }
    }

    public void bufferWrite(int data){
        writeVRAM(vramAddr.reg , data);
        vramAddr.setAddressRegister(vramAddr.reg + (control.Increment ? 32 : 1));
    }

    public int readVRAM(int addr) {
        addr &= 0x3FFF; // Mask to target the nametable addressable range
        // The CHR ROM is a part of VRAM, located from 0x0000 to 0x1fff.
        if (addr <= 0x1FFF)
            return nes.mapper.readVROM(addr);
        else if (addr <= 0x3EFF) {
            addr &= 0x0FFF;
            switch (nes.rom.getMirror()) {
                // Vertical Scrolling
                case VERTICAL:
                    if (addr <= 0x03FF)
                        return nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x07FF)
                        return nameTable[1][addr & 0x03FF];
                    else if (addr <= 0x0BFF)
                        return nameTable[0][addr & 0x03FF];
                    else
                        return nameTable[1][addr & 0x03FF];
                    // Horizontal Scrolling
                case HORIZONTAL:
                    if (addr <= 0x03FF)
                        return nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x07FF)
                        return nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x0BFF)
                        return nameTable[1][addr & 0x03FF];
                    else
                        return nameTable[1][addr & 0x03FF];
                case FOUR_SCREEN:
                    break;
                case SINGLE:
                    if (addr <= 0x03FF)
                        return nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x07FF)
                        return nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x0BFF)
                        return nameTable[0][addr & 0x03FF];
                    else
                        return nameTable[0][addr & 0x03FF];
                default:
                    return 0;
            }
        }
        // Addr >= $3F00 <= $3FFF
        else {
            addr &= 0x001F; // Mask to target the 32 byte addr range
            switch (addr) {
                case 0x10 -> addr = 0x0;
                case 0x14 -> addr = 0x4;
                case 0x18 -> addr = 0x8;
                case 0x1C -> addr = 0xC;
            }
            return paletteTable[addr] & (mask.grayScale ? 0x30 : 0x3F);
        }
        return 0;
    }

    public int getColorPalette(int palette, int point) {
        // Palette addressable range is $3F00 to $3F1F
        int tmp = readVRAM(0x3F09 + (palette << 2) + point) & 0x3F;
        System.out.println(tmp);
        return paletteTable[tmp];
    }

    public void setStatusFlag(Status flag, boolean b) {
        nes.cpu.ram[0x2002] = b ? nes.cpu.ram[0x2002] | flag.bit : nes.cpu.ram[0x2002] & ~flag.bit;
    }

    public int getStatusRegister() {
        setStatusFlag(Status.VBLANK, false);
        firstWrite = false;
        return nes.cpu.ram[0x2002];
    }
    public void clock() {
        // Target the 240 scanline range only
        if (scanline >= -1 && scanline < 240) {
            if (scanline == 0 && cycles == 0)
                // Odd frame skip
                cycles = 1;
            // scanline at -1 used to configure shifters
            if (scanline == -1 && cycles == 1)
                setStatusFlag(Status.VBLANK, false);
            if ((cycles >= 2 && cycles < 258) || (cycles >= 321 && cycles < 338)) {
                // Update Shifters
                if (mask.Background || mask.Sprite) {
                    // Background tile pattern
                    shifter.patternLow <<= 1;
                    shifter.patternHigh <<= 1;

                    // Palette attributes
                    shifter.attrLow <<= 1;
                    shifter.attrHigh <<= 1;
                }
                // Render background through a repeatable sequence every 2 cycles
                switch ((cycles - 1) % 8) {
                    case 0:
                        // These shifters shift 1 bit along
                        // Its 16 bits wide so the top 8 bits are the current 8 pixels being drawn and
                        // the bottom 8 pixels are the next 8 pixels to be drawn.
                        updateBackgroundShifters();
                        shifter.nextID = readVRAM(0x2000 | (vramAddr.reg & 0x0FFF));
                        break;
                    case 2:
                        // All attribute mem begins wihtin a nametable, so OR with
                        shifter.nextAtrr = readVRAM(
                                0x23C0 | ((vramAddr.nametableY ? 1 : 0) << 11) | ((vramAddr.nametableX ? 1 : 0) << 10)
                                        | ((vramAddr.coarseY >> 2) << 3) | (vramAddr.coarseX >> 2));
                        if ((vramAddr.coarseY & 2) > 0)
                            shifter.nextAtrr >>= 4;
                        if ((vramAddr.coarseX & 2) > 0)
                            shifter.nextAtrr >>= 2;
                        shifter.nextAtrr &= 3;
                        break;
                    case 4:
                        // Get the bottom half of the next background tile bit
                        shifter.nextLow = readVRAM(
                                ((control.backgroundPattern ? 1 : 0) << 12) + (shifter.nextID << 4) + vramAddr.fineY);
                        break;
                    case 6:
                        // Get the page of the next background tile bit
                        shifter.nextHigh = readVRAM(((control.backgroundPattern ? 1 : 0) << 12) + (shifter.nextID << 4)
                                + (vramAddr.fineY + 8));
                        break;
                    case 7:
                        // Increment background column horizontally
                        if (mask.Background || mask.Sprite) {
                            if (vramAddr.coarseX == 31) {
                                vramAddr.coarseX = 0;
                                vramAddr.nametableX = !vramAddr.nametableX; //Switch horizontal nametable
                            } else
                                vramAddr.coarseX++;
                        }
                }
            }
            //End of scanline
            //Since the frame is (256) * 240
            if (cycles == 256 && (mask.Background || mask.Sprite)) {
                //Y increment
                if (vramAddr.fineY < 7)
                    vramAddr.fineY++;
                else {
                    //reset fine Y
                    vramAddr.fineY = 0;
                    // coarse Y = 0
                    // switch vertical nametable
                    // coarse Y = 0, nametable not switched
                    // Increment coarse Y and  put coarse Y back into v
                    switch (vramAddr.coarseY) {
                        case 29 -> {
                            vramAddr.coarseY = 0;
                            vramAddr.nametableY = !vramAddr.nametableY;
                        }
                        case 31 -> vramAddr.coarseY = 0;
                        default -> vramAddr.coarseY++;
                    }
                }
            }

            if (cycles == 257) {
                updateBackgroundShifters();
                //TAX
                if (mask.Background || mask.Sprite) {
                    vramAddr.nametableX = tmpAddr.nametableX;
                    vramAddr.coarseX = tmpAddr.coarseX;
                }
            }

            if (cycles == 338 || cycles == 340)
                shifter.nextID = readVRAM(0x2000 | (vramAddr.reg & 0x0FFF));

            if (scanline == -1 && cycles >= 280 && cycles <= 304) {
                //TAY
                if (mask.Background || mask.Sprite) {
                    vramAddr.fineY = tmpAddr.fineY;
                    vramAddr.nametableY = tmpAddr.nametableY;
                    vramAddr.coarseY = tmpAddr.coarseY;
                }
            }
        }

        //End of frame set VBLANK
        if (scanline >= 241 && scanline <= 260) {
            if (scanline == 241 && cycles == 1)
                setStatusFlag(Status.VBLANK, true);
        }
        int palette = 0;
        int point = 0;
        if (mask.Background) {
            int fineX = 0x8000 >> xOffset;
            int pointLow = shifter.patternLow & fineX;
            int pointHigh = shifter.patternHigh & fineX;

            point = (pointHigh << 1) | pointLow;

            int backgroundPaletteLow = shifter.attrLow & fineX;
            int backgroundPaletteHigh = shifter.attrHigh & fineX;
            palette = (backgroundPaletteHigh << 1) | backgroundPaletteLow;
        }

        if(cycles > 0 && cycles < 257 && scanline >= 0 && scanline < 240)
            curFrame[(cycles - 1) + (scanline * 256)] |= getColorPalette(palette, point);
        cycles++;
        if (cycles > 340) {
            cycles = 0;
            scanline++;
            if (scanline > 260) {
                scanline = -1;
                complete = true;
            }
        }
    }
}
