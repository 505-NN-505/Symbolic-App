package org.compilation.terror;

public class SymbolResult {
    private int mNumber;
    private static float maxProb;

    public SymbolResult(float[] probs) {
        maxProb = 0.0f;
        mNumber = argmax(probs);
    }

    public int getNumber() {
        return mNumber;
    }
    public float getMaxProb() {
        return maxProb;
    }

    private static int argmax(float[] probs) {
        int maxIdx = -1;
        maxProb = 0.0f;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb) {
                maxProb = probs[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }
}
