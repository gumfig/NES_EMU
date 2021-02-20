package gumfig.com;

import javax.lang.model.type.UnionType;

public class Ppu {
    //Variables
    Nes nes;
    //2-1KB area of memory used by the PPU to lay out backgrounds.
    //This could be assigned to 4kb though.
    private int[][] nameTable;
    //Palette data: $3F00 -> $3F1F
    private int[] imgPalette, sprPalette;
    //Misc. variables using in rendering
    private int scanline, cycle;
    private oamEntry[] OAM; //Internal mem in PPU that contains a display list of up to 64 sprites.
    private int oamAddr; //$2003 OAMADDR initially set to $00
    //The 4 bytes assigned to a single Object in the OAM
    private class oamEntry{
        public int x, y; //Sprite X and Y pos
        public int attr; //Flags define how a sprite is rendered. Flipped Horizontally/Vertically
        public int id; //Tile number of sprite within pattern table
    };
    //PPU Registers
    //$2000 PPUCTRL
    public enum Control{
        //Base nametable address (0 = $2000; 1 = $2400; 2 = $2800; 3 = $2C00)
        //Equivalently, bits 1 and 0 are the most significant bit of the scrolling coordinates (see Nametables and PPUSCROLL):
        NAMETABLE_X(1),
        NAMETABLE_Y(1 << 1),
        INCREMENT(1 << 2),
        SPRITE_PATTERN(1 << 3),
        BACKGROUND_PATTERN(1 << 4),
        SPRITZE_SIZE(1 << 5),
        SLAVE(1 << 6),
        ENABLE_NMI(1 << 7);
        private final int bit;
        Control(int bit){
            this.bit = bit;
        }
    }
    //$2001 PPUMASK
    public enum Mask{
        GRAYSCALE(1),
        BACKGROUND_LEFT(1 << 1),
        SPRITE_LEFT(1 << 2),
        BACKGROUND(1 << 3),
        SPRITE(1 << 4),
        RED(1 << 5),
        GREEN(1 << 6),
        BLUE(1 << 7);

        private final int bit;
        Mask(int bit){this.bit = bit;};
    }
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
    //Init
    Ppu(Nes Nes){
        this.nes = Nes;
    }
    public void reset(){
        nameTable = new int[2][1024];
        imgPalette = new int[16];
        sprPalette = new int[16];

        OAM = new oamEntry[64];
        oamAddr = 0;

        scanline = 0;
    }
    public void setStatusFlag(Status stat, boolean b){
        nes.cpu.ram[0x2002] = b ? nes.cpu.ram[0x2002] | stat.bit : nes.cpu.ram[0x2002] & ~stat.bit;
    }
    public boolean getStatusFlag(Status stat){
        return ((nes.cpu.ram[0x2002] & stat.bit) > 0);
    }
}
