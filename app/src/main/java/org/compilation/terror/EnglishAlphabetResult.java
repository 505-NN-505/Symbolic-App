package org.compilation.terror;

import java.util.Arrays;

public class EnglishAlphabetResult {

    private int mNumber;

    public EnglishAlphabetResult(float[] probs) {
        mNumber = argmax(probs);
    }

    public int getNumber() {
        return mNumber;
    }

    private static int argmax(float[] probs) {
        int maxIdx = -1;
        float maxProb = 0.0f;
        System.out.println(probs.length);
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb) {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        System.out.println(maxIdx + " " + maxProb);
        System.out.println(probs['e' - 'a']);
        System.out.println("probs = " + Arrays.toString(probs));
        return maxIdx;
    }

}
