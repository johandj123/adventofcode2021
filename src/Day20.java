import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Day20 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(new File("input20.txt").toPath()).trim();
        String[] inputParts = input.split("\n\n");
        String lookupInput = inputParts[0];
        boolean[] lookup = new boolean[512];
        for (int i = 0; i < lookup.length; i++) {
            lookup[i] = lookupInput.charAt(i) == '#';
        }
        Image image = new Image(inputParts[1].split("\n"));
        Image image2 = image.enhance(lookup).enhance(lookup);
        System.out.println("First: " + image2.count());
        Image currentImage = image;
        for (int i = 0; i < 50; i++) {
            currentImage = currentImage.enhance(lookup);
        }
        System.out.println("Second: " + currentImage.count());
    }

    static class Image {
        boolean border;
        boolean[][] image;

        public Image(String[] inputImage) {
            image = new boolean[inputImage.length][inputImage[0].length()];
            for (int y = 0; y < image.length; y++) {
                for (int x = 0; x < image[0].length; x++) {
                    image[y][x] = (inputImage[y].charAt(x) == '#');
                }
            }
        }

        public Image(int width, int height, boolean border) {
            this.image = new boolean[height][width];
            this.border = border;
        }

        private boolean get(int x, int y) {
            if (x < 0 || y < 0 || x >= image[0].length || y >= image.length) {
                return border;
            } else {
                return image[y][x];
            }
        }

        private int getIndex(int x, int y) {
            int result = 0;
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    result = (result << 1) | (get(x + dx, y + dy) ? 1 : 0);
                }
            }
            return result;
        }

        public Image enhance(boolean[] lookup) {
            Image result = new Image(image.length + 2, image[0].length + 2, lookup[border ? 511 : 0]);
            for (int y = 0; y < result.image.length; y++) {
                for (int x = 0; x < result.image[0].length; x++) {
                    result.image[y][x] = lookup[getIndex(x - 1, y - 1)];
                }
            }
            return result;
        }

        public int count() {
            if (border) {
                return Integer.MAX_VALUE;
            }
            int result = 0;
            for (int y = 0; y < image.length; y++) {
                for (int x = 0; x < image[0].length; x++) {
                    if (image[y][x]) {
                        result++;
                    }
                }
            }
            return result;
        }
    }
}
