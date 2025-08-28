package dsalldmix;

import java.util.*;


/**
 * Problem Statement:
 * Given a playlist of songs, you have to design a song shuffler.
 * This song shuffler is not like the normal song shuffler that shuffles the complete playlist at the start and returns a shuffled list, but instead when asked for a next song to be played, returns a random song from the list of songs.
 * The next random song to be played should satisfy a condition that the song was not played in the last 'k' turns.
 * You have to make sure, that at each call, all the eligible (not played during last k turns) songs have equal probability of being played next.
 *
 * For example:
 * if songs = [A, B, C, D], k = 2
 */

public class MusicShuffler {

    public static class Shuffler {
        private final Map<Integer, String> songs;
        private final Deque<Integer> recent;
        private final Set<Integer> eligible;
        private final int k;
        private final Random random;

        public Shuffler(Map<Integer, String> musicList, int k) {
            this.songs = new HashMap<>(musicList);
            this.recent = new ArrayDeque<>();
            this.eligible = new HashSet<>(musicList.keySet());
            this.k = k;
            this.random = new Random();
        }

        public void play() {
            if (eligible.isEmpty()) {
                System.out.println("No eligible songs available!");
                return;
            }

            // Pick random from eligible set
            int idx = random.nextInt(eligible.size());
            Integer key = new ArrayList<>(eligible).get(idx);

            System.out.println("Playing: " + songs.get(key));

            // Update sets/queue
            eligible.remove(key);
            recent.addLast(key);
            if (recent.size() > k) {
                Integer old = recent.removeFirst();
                eligible.add(old);
            }
        }
    }


    public static void main(String[] args) {
        Map<Integer, String> musicList = new HashMap<>();
        musicList.put(0, "A");
        musicList.put(1, "B");
        musicList.put(2, "C");
        musicList.put(3, "D");

        Shuffler shuffler = new Shuffler(musicList, 2);
        for (int i = 1; i <= 10; i++) {
            shuffler.play();
        }
    }
}
