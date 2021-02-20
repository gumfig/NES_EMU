package gumfig.com;

public class Ppu {
    //Variables
    Nes nes;
    //2-1KB area of memory used by the PPU to lay out backgrounds.
    //This could be assigned to 4kb though.
    private int[] nameTable;
    //Palette data: $3F00 -> $3F1F
    private int[] imgPalette, sprPalette;
    //Misc. variables using in rendering
    private int scanline;
    private boolean vBlank; //Set to true when the PPU is idle.
    private class oamEntry{
        public int x, y; //Sprite X and Y pos
        public int attr; //Flags define how a sprite is rendered. Flipped Horizontally/Vertically
        public int id; //Tile number of sprite within pattern table
    }
    
    private oamEntry OAM;

    //Init
    Ppu(Nes Nes){
        this.nes = Nes;
    }
    public void reset(){
        nameTable = new int[1024 * 2]; //2kb
        imgPalette = new int[16];
        sprPalette = new int[16];
    }
    public void setStatusFlag(int data){
        
    }
    //Check vblank to send NMI req
    public boolean isVBlank(){
        return vBlank;
    }
}
