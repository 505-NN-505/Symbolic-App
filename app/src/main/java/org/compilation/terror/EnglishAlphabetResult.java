package org.compilation.terror;

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
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb) {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }

}
