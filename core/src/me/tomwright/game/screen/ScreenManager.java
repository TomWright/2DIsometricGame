package me.tomwright.game.screen;

public class ScreenManager {

    private static Screen currentScreen;

    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    public static void setCurrentScreen(Screen currentScreen) {
        ScreenManager.currentScreen = currentScreen;
    }

    public static boolean hasCurrentScreen() {
        return ScreenManager.currentScreen != null;
    }

}
