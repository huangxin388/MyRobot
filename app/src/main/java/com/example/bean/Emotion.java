package com.example.bean;

public class Emotion {
    private RobotEmotion robotEmotion;
    private UserEmotion userEmotion;
    public static class RobotEmotion {
        private int a;
        private int d;
        private int emotionId;
        private int p;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getD() {
            return d;
        }

        public void setD(int d) {
            this.d = d;
        }

        public int getEmotionId() {
            return emotionId;
        }

        public void setEmotionId(int emotionId) {
            this.emotionId = emotionId;
        }

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }
    }

    public static class UserEmotion {
        private int a;
        private int d;
        private int emotionId;
        private int p;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getD() {
            return d;
        }

        public void setD(int d) {
            this.d = d;
        }

        public int getEmotionId() {
            return emotionId;
        }

        public void setEmotionId(int emotionId) {
            this.emotionId = emotionId;
        }

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }
    }
}
