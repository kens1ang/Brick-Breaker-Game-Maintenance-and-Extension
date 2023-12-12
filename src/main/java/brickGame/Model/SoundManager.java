package brickGame.Model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
/**
 * Manages the sound effects and background music for the Breakout game.
 */
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
    /**
     * Initializes the sound effects and background music.
     */
    public SoundManager() {
        new Thread(this::initSounds).start();
    }
    /**
     * Initializes the sound effects and background music.
     */
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
    /**
     * Loads a sound file and creates a MediaPlayer for that sound.
     *
     * @param soundFileName The name of the sound file.
     * @return A MediaPlayer for the specified sound file.
     * @throws RuntimeException If the sound file is not found.
     */
    private MediaPlayer loadSound(String soundFileName) {
        URL resource = getClass().getResource("/" + soundFileName);
        if (resource == null) {
            throw new RuntimeException("Sound file not found: " + soundFileName);
        }
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(resource.toString()));
        mediaPlayer.setOnEndOfMedia(mediaPlayer::stop);
        return mediaPlayer;
    }
    /**
     * Plays the sound effect for a bomb hitting the paddle.
     */
    public void playBombHitSound() {
        playSound(bombhitpaddle);
    }
    /**
     * Plays the sound effect for collecting a bonus.
     */
    public void playCollectBonusSound(){
        playSound(collectbonus);
    }
    /**
     * Plays the sound effect for collecting a heart.
     */
    public void playCollectHeartSound(){
        playSound(collectheart);
    }
    /**
     * Plays the sound effect for losing a heart.
     */
    public void playMinusHeartSound(){
        playSound(minusheart);
    }
    /**
     * Plays the sound effect for leveling up.
     */
    public void playLevelUpSound(){
        playSound(levelup);
    }
    /**
     * Plays the sound effect for hitting a block.
     */
    public void playBlockHitSound(){
        playSound(blockhit);
    }
    /**
     * Plays the sound effect for hitting a star.
     */
    public void playHitStarSound(){
        playSound(hitstar);
    }
    /**
     * Plays the sound effect for a game over.
     */
    public void playGameOverSound(){
        playSound(gameover);
    }
    /**
     * Plays the sound effect for winning the game.
     */
    public void playYouWinSound(){
        playSound(youwin);
    }
    /**
     * Plays the sound effect for pausing the game.
     */
    public void playPauseSound(){playSound(pause);}
    /**
     * Plays the background music continuously.
     */
    public void playBackgroundMusic(double volume) {
        if (backgroundMusic != null) {
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusic.setVolume(volume);
            backgroundMusic.play();
            isBackgroundMusicPlaying = true;
        }
    }
    /**
     * Pauses the background music.
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }
    /**
     * Resumes the background music if it was playing before.
     */
    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && isBackgroundMusicPlaying) {
            backgroundMusic.play();
        }
    }
    /**
     * Plays a sound effect.
     *
     * @param sound The MediaPlayer for the sound effect.
     */
    private void playSound(MediaPlayer sound) {
        if (sound.getStatus() == MediaPlayer.Status.PLAYING) {
            sound.stop();
        }
        sound.play();
    }
}
