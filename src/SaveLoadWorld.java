import java.io.*;

class SaveLoadWorld {

   private static BufferedWriter writer;
   private static BufferedReader reader;

    static void save(World world, String fileName){
        try {
             writer = new BufferedWriter(new FileWriter("./src/Worlds/"+fileName));
            for(Platform p : world.platforms){
                if(p.getHeight() > 0 && p.getWidth() > 0) {
                    writer.write(p.getX() + "#" + p.getY() + "#" + p.getWidth() + "#" + p.getHeight());
                    writer.newLine();
                }
            }
            writer.write("#" + 0 + "#" + 100 + "#" + world.player.getWidth() + "#" + world.player.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void load(String fileName, World world){
        try {
            reader = new BufferedReader(new FileReader("./src/Worlds/"+fileName));
            String line;
            String[] values;

            while ((line = reader.readLine()) != null){
                values = line.split("#");
                if(!values[0].equals("")){
                    world.addNewPlatform((int)Double.parseDouble(values[0]), (int)Double.parseDouble(values[1]), (int)Double.parseDouble(values[2]), (int)Double.parseDouble(values[3]));
                }else {
                    world.initialisePlayer((int) Double.parseDouble(values[1]), (int) Double.parseDouble(values[2]), (int) Double.parseDouble(values[3]), (int) Double.parseDouble(values[4]));
                    world.player.getR().toFront();
                }
            }
        } catch (FileNotFoundException fileNotFound) {
            world.addDefaultStartBuild();
            save(world, Main.worldName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                load(Main.worldName, world);
            }
        }
    }

}