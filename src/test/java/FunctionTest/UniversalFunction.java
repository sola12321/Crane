package FunctionTest;

import cs425.crane.function.Mp4Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * A demo function for MP4 function processing module
 * Must NOT have a package name
 * Must implement Mp4Function interface
 * A pre-compiled version (MD5: 41a1eeaa9a00fddbefc130b5a808df83) is in the root of project directory for testing
 */
public class UniversalFunction implements Mp4Function {

    private static final Random r = new Random();

    private static List<Integer> genList(int size) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            res.add(r.nextInt(10000));
        }
        return res;
    }

    public Function<Integer, List<Integer>> mp4Task() {
        return input -> {
            return genList(input);
        };
    }

}
