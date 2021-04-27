package gumfig.com;

import java.util.Arrays;

public class Ppu {
    // Variables
    Nes nes;
    // 2-1KB area of memory used by the PPU to lay out backgrounds.
    // This could be assigned to 4kb though.
    private int[][] nameTable, sprPatternTable, sprNameTable;
    // Palette data: $3F00 -> $3F1F
    // 64 bytes representing colors
    private final int[] palette;
    private int paletteTable[];
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
    public Status status;
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
            //this.Background = (val & (1 << 3)) > 0;
            this.Background = true;
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
    static class Status {
        boolean vBlank, sprOverflow, spr0Hit;
        int UNUSED;

        Status() {
            setRegister(0);
        }

        public void setRegister(int val) {
            UNUSED = val & 0x1F;
            sprOverflow = ((val >> 5) & 1) > 0;
            spr0Hit = ((val >> 6) & 1) > 0;
            vBlank = ((val >> 7) & 1) > 0;
        }

        public int getRegister() {
            return UNUSED | ((sprOverflow ? 1 : 0) << 5) | ((spr0Hit ? 1 : 0) << 6) | ((vBlank ? 1 : 0) << 7);
        }
    }

    // Current VRAM address and temporary VRAM address (15 bits)
    static class AddressRegister {
        int coarseX = 0, coarseY = 0, fineY = 0;
        // Nametable Select
        boolean nametableX = false, nametableY = false;

        int getRegister() {
            return ((coarseX) |
                    (coarseY << 5) |
                    ((nametableX ? 1 : 0) << 10) |
                    ((nametableY ? 1 : 0) << 11) |
                    (fineY << 12));
        }

        void setAddressRegister(int val) {
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
        /*
        palette = new int[]{0x525252, 0xB40000, 0xA00000, 0xB1003D, 0x740069, 0x00005B, 0x00005F, 0x001840,
                0x002F10, 0x084A08, 0x006700, 0x124200, 0x6D2800, 0, 0, 0, 0xC4D5E7, 0xFF4000, 0xDC0E22, 0xFF476B,
                0xD7009F, 0x680AD7, 0x0019BC, 0x0054B1, 0x006A5B, 0x008C03, 0x00AB00, 0x2C8800, 0xA47200, 0, 0, 0,
                0xF8F8F8, 0xFFAB3C, 0xFF7981, 0xFF5BC5, 0xFF48F2, 0xDF49FF, 0x476DFF, 0x00B4F7, 0x00E0FF, 0x00E375,
                0x03F42B, 0x78B82E, 0xE5E218, 0x787878, 0, 0, 0xFFFFFF, 0xFFF2BE, 0xF8B8B8, 0xF8B8D8, 0xFFB6FF,
                0xFFC3FF, 0xC7D1FF, 0x9ADAFF, 0x88EDF8, 0x83FFDD, 0xB8F8B8, 0xF5F8AC, 0xFFFFB0, 0xF8D8F8, 0, 0};

         */
        palette = new int[64];
        palette[0x00] = getRgb(84, 84, 84);
        palette[0x01] = getRgb(0, 30, 116);
        palette[0x02] = getRgb(8, 16, 144);
        palette[0x03] = getRgb(48, 0, 136);
        palette[0x04] = getRgb(68, 0, 100);
        palette[0x05] = getRgb(92, 0, 48);
        palette[0x06] = getRgb(84, 4, 0);
        palette[0x07] = getRgb(60, 24, 0);
        palette[0x08] = getRgb(32, 42, 0);
        palette[0x09] = getRgb(8, 58, 0);
        palette[0x0A] = getRgb(0, 64, 0);
        palette[0x0B] = getRgb(0, 60, 0);
        palette[0x0C] = getRgb(0, 50, 60);
        palette[0x0D] = getRgb(0, 0, 0);
        palette[0x0E] = getRgb(0, 0, 0);
        palette[0x0F] = getRgb(0, 0, 0);

        palette[0x10] = getRgb(152, 150, 152);
        palette[0x11] = getRgb(8, 76, 196);
        palette[0x12] = getRgb(48, 50, 236);
        palette[0x13] = getRgb(92, 30, 228);
        palette[0x14] = getRgb(136, 20, 176);
        palette[0x15] = getRgb(160, 20, 100);
        palette[0x16] = getRgb(152, 34, 32);
        palette[0x17] = getRgb(120, 60, 0);
        palette[0x18] = getRgb(84, 90, 0);
        palette[0x19] = getRgb(40, 114, 0);
        palette[0x1A] = getRgb(8, 124, 0);
        palette[0x1B] = getRgb(0, 118, 40);
        palette[0x1C] = getRgb(0, 102, 120);
        palette[0x1D] = getRgb(0, 0, 0);
        palette[0x1E] = getRgb(0, 0, 0);
        palette[0x1F] = getRgb(0, 0, 0);

        palette[0x20] = getRgb(236, 238, 236);
        palette[0x21] = getRgb(76, 154, 236);
        palette[0x22] = getRgb(120, 124, 236);
        palette[0x23] = getRgb(176, 98, 236);
        palette[0x24] = getRgb(228, 84, 236);
        palette[0x25] = getRgb(236, 88, 180);
        palette[0x26] = getRgb(236, 106, 100);
        palette[0x27] = getRgb(212, 136, 32);
        palette[0x28] = getRgb(160, 170, 0);
        palette[0x29] = getRgb(116, 196, 0);
        palette[0x2A] = getRgb(76, 208, 32);
        palette[0x2B] = getRgb(56, 204, 108);
        palette[0x2C] = getRgb(56, 180, 204);
        palette[0x2D] = getRgb(60, 60, 60);
        palette[0x2E] = getRgb(0, 0, 0);
        palette[0x2F] = getRgb(0, 0, 0);

        palette[0x30] = getRgb(236, 238, 236);
        palette[0x31] = getRgb(168, 204, 236);
        palette[0x32] = getRgb(188, 188, 236);
        palette[0x33] = getRgb(212, 178, 236);
        palette[0x34] = getRgb(236, 174, 236);
        palette[0x35] = getRgb(236, 174, 212);
        palette[0x36] = getRgb(236, 180, 176);
        palette[0x37] = getRgb(228, 196, 144);
        palette[0x38] = getRgb(204, 210, 120);
        palette[0x39] = getRgb(180, 222, 120);
        palette[0x3A] = getRgb(168, 226, 144);
        palette[0x3B] = getRgb(152, 226, 180);
        palette[0x3C] = getRgb(160, 214, 228);
        palette[0x3D] = getRgb(160, 162, 160);
        palette[0x3E] = getRgb(0, 0, 0);
        palette[0x3F] = getRgb(0, 0, 0);
        paletteTable = new int[32];
        this.nes = Nes;
    }
    public int getRgb(int r, int g, int b){
        return (r << 16) | (g << 8) | b;
    }
    // Init
    public void reset() {
        nameTable = new int[2][1024];
        sprPatternTable = new int[2][128 * 128];
        sprNameTable = new int[2][256 * 240];

        OAM = new oamEntry[64];
        oamAddr = 0;

        cycles = 0;
        scanline = -1;
        oddFrame = false;

        shifter = new BackgroundShifter();

        mask = new Mask();
        control = new Control();
        status = new Status();

        vramAddr = new AddressRegister();
        tmpAddr = new AddressRegister();

        curFrame = new int[256 * 240];

        buffer = 0;
    }

    private void updateBackgroundShifters() {
        shifter.patternLow &= (0xFF00 | shifter.nextLow);
        shifter.patternHigh &= (0xFF00 | shifter.nextHigh);

        // Attribute bits change every 8 pixels and are synchronised with the pattern
        // shifters.
        shifter.attrLow &= 0xFF00 | ((shifter.nextAtrr & 1) > 0 ? 0xFF : 0);
        shifter.attrHigh &= 0xFF00 | ((shifter.nextAtrr & 2) > 0 ? 0xFF : 0);
    }

    public void setOamAddr(int data) {
        oamAddr = data;
    }

    public void updateSpritePatternTable(int index, int pal) {
        // The pattern table is divided into two 256-tile sections 16x16
        // Each tile in the pattern table is 16 bytes which are separated to left and right planes
        // Each plane is 8x8 bits
        // The first plane controls bit 0 of the color. 
        // the second plane controls bit 1. Any pixel whose color is 0 is background/transparent
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                // X here represents a tile and Y represents the 256-tile section
                int offset = y * 256 + x * 16;
                for (int i = 0; i < 8; i++) {
                    //Get the first plane
                    int low = readVRAM((index * 0x1000) + i + offset);
                    //Get the second plane
                    int high = readVRAM((index * 0x1000) + i + offset + 8);
                    for (int j = 0; j < 8; j++) {
                        int point = (low & 0x1) + (high & 0x1);
                        low >>= 1;
                        high >>= 1;
                        sprPatternTable[index][(x * 8 + (7 - j)) + (((y * 8) + i) * 128)] = getColorPalette(pal, point);
                    }
                }
            }
        }
    }

    public int getBuffer() {
        int tmp = buffer;
        buffer = readVRAM(vramAddr.getRegister());
        if (vramAddr.getRegister() >= 0x3F00) tmp = buffer;
        vramAddr.setAddressRegister((vramAddr.getRegister() + (control.Increment ? 32 : 1)) & 0xFFFF);
        System.out.println("vram: " + vramAddr.getRegister());
        return tmp;
    }

    public void bufferWrite(int data) {
        writeVRAM(vramAddr.getRegister(), data);
        vramAddr.setAddressRegister((vramAddr.getRegister() + (control.Increment ? 32 : 1)) & 0xFFFF);
    }

    public void controlWrite(int data) {
        control.setRegister(data);
        tmpAddr.nametableX = control.nametableX;
        tmpAddr.nametableY = control.nametableY;
    }

    public void scrollWrite(int data) {
        if (firstWrite) {
            //Second write
            tmpAddr.fineY = data & 0x7;
            tmpAddr.coarseY = data >> 3;
            firstWrite = false;
        } else {
            xOffset = data & 0x7;
            tmpAddr.coarseX = data >> 3;
            firstWrite = true;
        }
    }

    public void addressWrite(int data) {
        if (firstWrite) {
            tmpAddr.setAddressRegister((tmpAddr.getRegister() & 0xFF00) | data);
            vramAddr.setAddressRegister(tmpAddr.getRegister());
            firstWrite = false;
        } else {
            tmpAddr.setAddressRegister(((data & 0x3F) << 8) | (tmpAddr.getRegister() & 0xFF));
            firstWrite = true;
        }
    }

    public int[] getPatternTable(int index, int palette) {
        updateSpritePatternTable(index, palette);
        return sprPatternTable[index];
    }

    public int readVRAM(int addr) {
        //addr &= 0x3FFF; // Mask to target the nametable addressable range
        // The CHR ROM is a part of VRAM, located from 0x0000 to 0x1fff.
        addr &= 0x3FFF;
        int tmp = 0;

        if (addr <= 0x1FFF)
            tmp = nes.mapper.readVROM(addr);
        else if (addr <= 0x3EFF) {
            //System.out.println(Integer.toHexString(addr));
            addr &= 0x0FFF;
            switch (nes.rom.getMirror()) {
                // Vertical Scrolling
                case VERTICAL:
                    if (addr <= 0x03FF)
                        tmp = nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x07FF)
                        tmp = nameTable[1][addr & 0x03FF];
                    else if (addr <= 0x0BFF)
                        tmp = nameTable[0][addr & 0x03FF];
                    else
                        tmp = nameTable[1][addr & 0x03FF];
                    break;
                    // Horizontal Scrolling
                case HORIZONTAL:
                    if (addr <= 0x03FF)
                        tmp = nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x07FF)
                        tmp = nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x0BFF)
                        tmp = nameTable[1][addr & 0x03FF];
                    else
                        tmp = nameTable[1][addr & 0x03FF];
                    break;
                case FOUR_SCREEN:
                    break;
                case SINGLE:
                    if (addr <= 0x03FF)
                        tmp = nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x07FF)
                        tmp = nameTable[0][addr & 0x03FF];
                    else if (addr <= 0x0BFF)
                        tmp = nameTable[0][addr & 0x03FF];
                    else
                        tmp = nameTable[0][addr & 0x03FF];
                    break;
            }
        }
        // Addr >= $3F00 <= $3FFF
        else {
            addr &= 0x1F; // Mask to target the 32 byte addr range
            if (addr == 0x10) addr = 0;
            if (addr == 0x14) addr = 4;
            if (addr == 0x18) addr = 8;
            if (addr == 0x1C) addr = 0xC;
            tmp = paletteTable[addr] & (mask.grayScale ? 0x30 : 0x3F);
        }
        return tmp & 0xFF;
    }

    public void writeVRAM(int addr, int data) {
        addr &= 0x3FFF;
        data &= 0xFF;
        /*
        if (addr <= 0x1FFF)
            nes.mapper.writeCHR(addr, data);
         */

        if (addr <= 0x3EFF) {
            addr &= 0x0FFF;
            switch (nes.rom.getMirror()) {
                // Vertical Scrolling
                case VERTICAL:
                    if (addr <= 0x03FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x07FF) nameTable[1][addr & 0x03FF] = data;
                    else if (addr <= 0x0BFF) nameTable[0][addr & 0x03FF] = data;
                    else nameTable[1][addr & 0x03FF] = data;
                    break;
                    // Horizontal Scrolling
                case HORIZONTAL:
                    if (addr <= 0x03FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x07FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x0BFF) nameTable[1][addr & 0x03FF] = data;
                    else nameTable[1][addr & 0x03FF] = data;
                    break;
                case FOUR_SCREEN:
                    break;
                case SINGLE:
                    if (addr <= 0x03FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x07FF) nameTable[0][addr & 0x03FF] = data;
                    else if (addr <= 0x0BFF) nameTable[0][addr & 0x03FF] = data;
                    else nameTable[0][addr & 0x03FF] = data;
                    break;
            }
        } else {
            addr &= 0x1F;
            if (addr == 0x10) addr = 0;
            if (addr == 0x14) addr = 4;
            if (addr == 0x18) addr = 8;
            if (addr == 0x1C) addr = 0xC;
            paletteTable[addr] = data;
        }
    }


    public int getColorPalette(int pal, int point) {
        // Palette addressable range is $3F00 to $3F1F
        int tmp = readVRAM(0x3F00 + (pal << 2) + point);
        return palette[tmp & 0x3F];
    }

    public int getStatusRegister() {
        firstWrite = false;
        int tmp = (status.getRegister() & 0xE0) | (buffer & 0x1F);
        status.vBlank = false;
        return tmp;
    }

    public void clock() {
        // Target the 240 scanline range only
        if (scanline >= -1 && scanline < 240) {
            if (scanline == 0 && cycles == 0)
                // Odd frame skip
                cycles = 1;
            // scanline at -1 used to configure shifters
            if (scanline == -1 && cycles == 1)
                status.vBlank = false;
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
                        shifter.nextID = readVRAM(0x2000 | (vramAddr.getRegister() & 0x0FFF));
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
                        shifter.nextHigh = readVRAM(((control.backgroundPattern ? 1 : 0) << 12) + (shifter.nextID << 4) + (vramAddr.fineY + 8));
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
                shifter.nextID = readVRAM(0x2000 | (vramAddr.getRegister() & 0x0FFF));

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
                status.vBlank = true;
        }
        int pal = 0;
        int point = 0;
        if (mask.Background) {
            int fineX = 0x8000 >> xOffset;
            int pointLow = shifter.patternLow & fineX;
            int pointHigh = shifter.patternHigh & fineX;

            point = (pointHigh << 1) | pointLow;

            int backgroundPaletteLow = shifter.attrLow & fineX;
            int backgroundPaletteHigh = shifter.attrHigh & fineX;
            pal = (backgroundPaletteHigh << 1) | backgroundPaletteLow;
        }



        if (cycles > 0 && cycles < 257 && scanline >= 0 && scanline < 240)
            curFrame[(cycles - 1) + (scanline * 256)] = getColorPalette(pal, point);
        //curFrame[(cycles - 1) + (scanline * 256)] = paletteTable[(int) (Math.random() * 0x3F)];

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
