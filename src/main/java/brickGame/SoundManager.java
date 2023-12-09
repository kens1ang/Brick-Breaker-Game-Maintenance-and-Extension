package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {
    private MediaPlayer pause;
    private MediaPlayer bombhitpaddle;
    private MediaPlayer collectbonus;
    private MediaPlayer collectheart;
    private MediaPlayer minusheart;
    private MediaPlayer levelup;
    private MediaPlayer blockhit;
    private MediaPlayer hitstar;
    private MediaPlayer gameover;
    private MediaPlayer youwin;
    private MediaPlayer backgroundMusic;
    private boolean isBackgroundMusicPlaying;

    public SoundManager() {
        new Thread(this::initSounds).start();
    }

    private void initSounds() {
        bombhitpaddle = loadSound("bomb.mp3");
        collectbonus = loadSound("bonus.wav");
        collectheart = loadSound("heart.mp3");
        minusheart = loadSound("minusheart.wav");
        levelup = loadSound("lvlup.wav");
        blockhit = loadSound("block2.wav");
        hitstar = loadSound("gold2.mp3");
        gameover = loadSound("gameover3.wav");
        youwin = loadSound("youwin.mp3");
        backgroundMusic = loadSound("bgmbb.mp3");
        pause = loadSound("pause.mp3");
        isBackgroundMusicPlaying = false;
    }

    private MediaPlayer loadSound(String soundFileName) {
        URL resource = getClass().getResource("/" + soundFileName);
        if (resource == null) {
            throw new RuntimeException("Sound file not found: " + soundFileName);
        }
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(resource.toString()));
        mediaPlayer.setOnEndOfMedia(mediaPlayer::stop);
        return mediaPlayer;
    }

    public void playBombHitSound() {
        playSound(bombhitpaddle);
    }
    public void playCollectBonusSound(){
        playSound(collectbonus);
    }
    public void playCollectHeartSound(){
        playSound(collectheart);
    }
    public void playMinusHeartSound(){
        playSound(minusheart);
    }
    public void playLevelUpSound(){
        playSound(levelup);
    }
    public void playBlockHitSound(){
        playSound(blockhit);
    }
    public void playHitStarSound(){
        playSound(hitstar);
    }
    public void playGameOverSound(){
        playSound(gameover);
    }
    public void playYouWinSound(){
        playSound(youwin);
    }
    public void playPauseSound(){playSound(pause);}
    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusic.play();
            isBackgroundMusicPlaying = true;
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && isBackgroundMusicPlaying) {
            backgroundMusic.play();
        }
    }

    private void playSound(MediaPlayer sound) {
        if (sound.getStatus() == MediaPlayer.Status.PLAYING) {
            sound.stop();
        }
        sound.play();
    }
}
