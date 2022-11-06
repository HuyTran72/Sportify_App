
// declares a class for the app
public class SpotifyLikeApp {

    // global variables for the app
    String status;
    Long position;
    static Clip audioClip;
    static JSONArray musicJson;

    // "main" makes this class a java app that can be executeda
    public static void main(final String[] args) {
        try {
            musicJson = (JSONArray) (new JSONParser()).parse(new FileReader("Mymusic.json"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        // create a scanner for user input
        Scanner input = new Scanner(System.in);

        String userInput = "";
        while (!userInput.equals("q")) {
            menu();

            // get input
            userInput = input.nextLine();

            // accept upper or lower case commands
            userInput.toLowerCase();

            // do something
            handleMenu(userInput);

        }
        // close the scanner
        input.close();

    }

    /*
     * displays the menu for the app
     */
    public static void menu() {

        System.out.println("---- SpotifyLikeApp ----");
        System.out.println("[H]ome");
        System.out.println("[S]earch by title");
        System.out.println("[L]ibrary");
        System.out.println("[F]avorites");
        System.out.println("[Q]uit");
        System.out.println("");
        System.out.print("Enter q to Quit:");

    }

    /*
     * handles the user input for the app
     */
    public static void handleMenu(String userInput) {

        switch (userInput) {

        case "h":
            System.out.println("-->Home<--");
            break;

        case "s":
            System.out.println("-->Enter the title you would like to play<--");

            // create a scanner for Song input
            Scanner sc = new Scanner(System.in);

            String songName = "";

            // get input
            String input = sc.nextLine();

            songName = input;

            // accept upper or lower case commands
            songName.toLowerCase();

            Clip clip = getSongAndPlay(songName);

            long t = 0L;

            AtomicReference<Object> ref = new AtomicReference<Object>(t);

            while (!input.equals("s")) {

                System.out.println("--> Enter [p] to pause <--");
                System.out.println("--> Enter [re] to resume <--");
                System.out.println("--> Enter [s] to stop <--");
                System.out.println("--> Enter [r] to Rewind <--");
                System.out.println("--> Enter [f] to Forward <--");
                System.out.println("--> Enter [fa] to Favorite <--");

                // get input
                input = sc.nextLine();

                // accept upper or lower case commands
                input.toLowerCase();

                // do something
                handleSong(clip, input, ref, songName);

            }

            break;

        case "l":
            System.out.println("-->Library<--");
            printLibrary();
            break;

        case "f":
            System.out.println("-->Favorites<--");
            showFavorites();
            break;

        case "q":
            System.out.println("-->Quit<--");
            break;

        default:
            break;
        }

    }

    /*
     * handles the user input for the app
     */
    public static void handleSong(Clip clip, String input, AtomicReference<Object> clipTime, String songName) {

        switch (input) {

        case "p":
            System.out.println("-->Pause<--");
            if (clip != null) {
                clipTime.set(clip.getMicrosecondPosition());
                clip.stop();
            }
            break;

        case "re":
            System.out.println("-->Resume<--");
            if (clip != null) {
                clip.setMicrosecondPosition((long) clipTime.get());
                clip.start();
            }
            break;

        case "s":
            System.out.println("-->Stop<--");
            clip.stop();
            break;

        case "r":
            System.out.println("-->Rewind<--");
            if (clip != null) {
                clip.stop();
                clip.setMicrosecondPosition(clip.getMicrosecondPosition() - 5000000);
                clip.start();
            }
            break;

        case "f":
            System.out.println("-->Forward<--");
            if (clip != null) {
                clip.stop();
                clip.setMicrosecondPosition(clip.getMicrosecondPosition() + 5000000);
                clip.start();
            }
            break;

        case "fa":
            System.out.println("-->Added to Favorite<--");

            try {

                for (Object obj : musicJson) {
                    JSONObject song = (JSONObject) obj;

                    String title = (String) song.get("title");

                    if (songName.equals(title)) {
                        song.put("IsFavorites", "true");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            break;

        default:
            break;
        }

    }

    }

    /*
     *
     * String filePath = (String) song.get("filePath"); System.out.println(filePath
     * + "\n");
     * 
     * System.out.println("=====================================\n\n"); }
     * 
     * } } catch (Exception e) { e.printStackTrace(); }
     * 
     * }
     * 
     * /* print library
     */
    public static void printLibrary() {

        try {
            for (Object obj : musicJson) {
                JSONObject song = (JSONObject) obj;

                String title = (String) song.get("title");
                System.out.println(title + "\n");

                String artist = (String) song.get("artist");
                System.out.println(artist + "\n");

                String year = (String) song.get("year");
                System.out.println(year + "\n");

                String genre = (String) song.get("genre");
                System.out.println(genre + "\n");

                String IsFavorites = (String) song.get("IsFavorites");
                System.out.println(IsFavorites + "\n");

                String filePath = (String) song.get("filePath");
                System.out.println(filePath + "\n");

                System.out.println("=====================================\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * plays an audio file
     */
    public static Clip play(String filePath) {

        final File file = new File(filePath);

        try {

            // create clip
            audioClip = AudioSystem.getClip();

            // get input stream
            final AudioInputStream in = getAudioInputStream(file);

            audioClip.open(in);
            audioClip.setMicrosecondPosition(0);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            return audioClip;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
