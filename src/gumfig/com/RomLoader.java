package gumfig.com;

import gumfig.com.Mapper.Mapper;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;

/*
Header (16 bytes)
  0-3: Constant $4E $45 $53 $1A ("NES" followed by MS-DOS end-of-file)
  4: Size of PRG ROM in 16 KB units
  5: Size of CHR ROM in 8 KB units (Value 0 means the board uses CHR RAM)
  6: Flags 6 - Mapper, mirroring, battery, trainer
  7: Flags 7 - Mapper, VS/Playchoice, NES 2.0
  8: Flags 8 - PRG-RAM size (rarely used extension)
  9: Flags 9 - TV system (rarely used extension)
  10: Flags 10 - TV system, PRG-RAM presence (unofficial, rarely used extension)
  11-15: Unused padding (should be filled with zero, but some rippers put their name across bytes 7-15)
 */


public class RomLoader {
    private int romSize, vromSize;
    public int[] prgRom, chrRom;
    public boolean trainer, battery, iNesFormat, nes2Format;
    public Mirror mirror = Mirror.UNLOADED;
    public Mapper mapper;
    private int id;

    public enum Mirror {
        UNLOADED, FOUR_SCREEN, HORIZONTAL, VERTICAL
    }

    RomLoader(File game) throws IOException, MapperException {
        byte[] data = Files.readAllBytes(game.toPath());
        iNesFormat = data[0] == 'N' && data[1] == 'E' && data[2] == 'S' && data[3] == 0x1A;
        nes2Format = iNesFormat && (data[7] & 0x0C) == 0x08;

        //Check for valid ines rom
        if (iNesFormat) {
            // Nes 2.0 Format
            if (nes2Format) {
                romSize = data[4] + ((data[9] & 15) << 8);
                vromSize = data[5] + ((data[9] >> 4) << 8);
            } else {
                romSize = data[4];
                vromSize = data[5];
            }

            mirror = (data[6] & 8) != 0 ? Mirror.FOUR_SCREEN : (data[6] & 1) != 0 ? Mirror.VERTICAL : Mirror.HORIZONTAL;
            battery = (data[6] & 2) != 0;
            trainer = (data[6] & 4) != 0;
            //Bottom 4 bits denote mapper number
            id = (data[6] >> 4) + ((data[7] >> 4) << 4);
            mapper = Mapper.getMapper(id);
            prgRom = new int[romSize * 16834]; // 1024 * 16
            chrRom = new int[vromSize * 8192]; // 1024 * 8

            int offset = 16 + (trainer ? 512 : 0); // Skip the header and trainer section


            //populate prgRom
            for(int i = 0; i < prgRom.length; i++) {
                prgRom[i] = data[i + offset];
                //System.out.println(prgRom[i]);
            }

            offset += prgRom.length; // Skip PRG_ROM section

            //populate chrRom
            for(int i = 0; i < chrRom.length; i++){
                if(offset + i >= data.length) break;
                chrRom[i] = data[i+offset];
                //System.out.println(chrRom[i]);
            }

            //Feed it to the garbage collector
            data = null;
        }else if (data[0] == 'N' && data[1] == 'E' && data[2] == 'S' && data[3] == 'M' &&data[4] == 0x1A){ // Valid nsf file
            // Don't know if im going to deal with this
            System.out.println("idk");
        }
        else throw new MapperException("Invalid file format!");
    }
    public Mirror getMirror(){
        return mirror;
    }
    @Override
    public String toString() {
        return "--------------ROM----------------" + '\n' +
                "Nes 2.0: " + nes2Format + "\n" +
                "rSize: " + romSize + " * 16kb" + "\n" +
                "vSize: " + vromSize + " * 8kb" + "\n" +
                "Mirroring: " + mirror.toString() + "\n" +
                "battery: " + battery + "\n" +
                "trainer: " + trainer + "\n" +
                "MapperID: " + id + "\n" +
                "Mapper: " + mapper.toString();
    }
}
