package com.guoyanchen.chinesechess;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.util.Objects;

public class BGMPlayer {

    private AdvancedPlayer bgmPlayer;
    private Thread bgmThread;

    public void playBGM() {
        System.out.println("play bgm");
        Runnable bgmPlayer = () -> {
            try {
                while (!Thread.interrupted()) {
                    this.bgmPlayer = new AdvancedPlayer(
                            Objects.requireNonNull(getClass().getResourceAsStream("bgm.mp3")),
                            FactoryRegistry.systemRegistry().createAudioDevice());
                    this.bgmPlayer.play();
                }
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        };

        bgmThread = new Thread(bgmPlayer);
        bgmThread.start();

    }

    public void stopBGM() {
        if (bgmThread != null && bgmThread.isAlive()) {
            bgmPlayer.close();
            bgmThread.interrupt();
            System.out.println("stop bgm");
        }
    }
}

