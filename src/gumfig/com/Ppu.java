package gumfig.com;

public class Ppu {
    //Variables
    Nes nes;
    //2-1KB area of memory used by the PPU to lay out backgrounds.
    //This could be assigned to 4kb though.
    private int[][] nameTable;
    //Palette data: $3F00 -> $3F1F
    private int[] imgPalette, sprPalette;
    //Misc. variables using in rendering
    private int scanline, cycles;
    private boolean oddFrame;
    private oamEntry[] OAM; //Internal mem in PPU that contains a display list of up to 64 sprites.
    private int oamAddr; //$2003 OAMADDR initially set to $00
    private Mask mask;
    private Control control;

    //The 4 bytes assigned to a single Object in the OAM
    private class oamEntry{
        public int x, y; //Sprite X and Y pos
        public int attribute; //Flags define how a sprite is rendered. Flipped Horizontally/Vertically
        public int id; //Tile index of sprite within pattern table
    };
    //PPU Registers
    //$2000 PPUCTRL
    private class Control{
        //Base nametable address (0 = $2000; 1 = $2400; 2 = $2800; 3 = $2C00)
        //Equivalently, bits 1 and 0 are the most significant bit of the scrolling coordinates (see Nametables and PPUSCROLL):
        boolean nameTableX = false, nameTableY = false, Increment = false, spritePattern = false, backgroundPattern = false, spriteSize = false, Slave = false, EnableNMI = false;
    }
    //$2001 PPUMASK
    private class Mask{ boolean grayScale = false, backgroundLeft = false, spriteLeft = false, Background = false, Sprite = false, Red = false, Green = false,Blue = false;}
    //$2002 PPUSTATUS
    public enum Status{
        UNUSED(1 << 4),      // Bit 0 -> 4 = Insignificant
        SPROVERFLOW(1 << 5), // Bit 5 = Sprite Overflow
        SPR0HIT(1 << 6),     // Bit 6 = Sprite 0 Hit
        VBLANK(1 << 7);      // Bit 7 = Vertical Blank
        private final int bit;
        Status(int bit){
            this.bit = bit;
        }
    }

    //Connect to Nes
    Ppu(Nes Nes){
        this.nes = Nes;
    }
    //Init
    public void reset(){
        nameTable = new int[2][1024];
        imgPalette = new int[16];
        sprPalette = new int[16];

        OAM = new oamEntry[64];
        oamAddr = 0;

        cycles = 0;
        scanline = -1;
        oddFrame = false;

        mask = new Mask();
        control = new Control();

    }
    public void clock(){
        cycles++;
        if(cycles > 340){
            cycles = 0;
            scanline++;
            if(scanline > 260){
                scanline = - 1;
                oddFrame = !oddFrame;
            }
        }
    }

    public void setOamAddr(int data){
        oamAddr = data;
    }

    public void renderScanline(){
        
    }
    public void updateControlRegister(int val){
        //Store the bit states in vars so i dont have to call a function everytime
        control.nameTableX        = (val & (1)) > 0;
        control.nameTableY        = (val & (1 << 1)) > 0;
        control.Increment         = (val & (1 << 2)) > 0;
        control.spritePattern     = (val & (1 << 3)) > 0;
        control.backgroundPattern = (val & (1 << 4)) > 0;
        control.spriteSize        = (val & (1 << 5)) > 0;
        control.Slave             = (val & (1 << 6)) > 0;
        control.EnableNMI         = (val & (1 << 7)) > 0;
    }
    public void updateMaskRegister(int val){
        //Store the bit states in vars so i dont have to call a function everytime
        mask.grayScale            = (val & (1)) > 0;
        mask.backgroundLeft       = (val & (1 << 1)) > 0;
        mask.spriteLeft           = (val & (1 << 2)) > 0;
        mask.Background           = (val & (1 << 3)) > 0;
        mask.Sprite               = (val & (1 << 4)) > 0;
        mask.Red                  = (val & (1 << 5)) > 0;
        mask.Green                = (val & (1 << 6)) > 0;
        mask.Blue                 = (val & (1 << 7)) > 0;
    }
    public void setStatusFlag(Status flag, boolean b){
        nes.cpu.ram[0x2002] = b ? nes.cpu.ram[0x2002] | flag.bit : nes.cpu.ram[0x2002] & ~flag.bit;
    }
    public int getStatusRegister(){
        setStatusFlag(Status.VBLANK, false);
        return nes.cpu.ram[0x2002];
    }
}
