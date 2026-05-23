package com.thanuboard.app;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;

public class TdBoardInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard qwertyKeyboard;
    private boolean isCaps = false;
    private StringBuilder inputBuffer = new StringBuilder();
    private HashMap<String, String> singlishMap;

    @Override
    public void onCreate() {
        super.onCreate();
        initSinglishDictionary();
    }

    @Override
    public View onCreateInputView() {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboardView = layout.findViewById(R.id.keyboard_view);
        qwertyKeyboard = new Keyboard(this, R.xml.qwerty_layout);
        keyboardView.setKeyboard(qwertyKeyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return layout;
    }

    private void initSinglishDictionary() {
        singlishMap = new HashMap<>();
        singlishMap.put("amma", "අම්මා");
        singlishMap.put("thaththa", "තාත්තා");
        singlishMap.put("kohomadha", "කොහොමද");
        singlishMap.put("subha", "සුබ");
        singlishMap.put("poya", "පෝය");
        singlishMap.put("malli", "මල්ලි");
        singlishMap.put("nanggi", "නංගි");
        singlishMap.put("oya", "ඔයා");
        singlishMap.put("lanka", "ලංකා");
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                if (inputBuffer.length() > 0) {
                    inputBuffer.deleteCharAt(inputBuffer.length() - 1);
                }
                ic.deleteSurroundingText(1, 0);
                break;

            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                qwertyKeyboard.setShifted(isCaps);
                keyboardView.invalidateAllKeys();
                break;

            case 32: 
                if (inputBuffer.length() > 0) {
                    String word = inputBuffer.toString().toLowerCase();
                    if (singlishMap.containsKey(word)) {
                        ic.deleteSurroundingText(word.length(), 0);
                        ic.commitText(singlishMap.get(word), 1);
                    }
                    inputBuffer.setLength(0);
                }
                ic.commitText(" ", 1);
                break;

            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;

            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code);
                }
                inputBuffer.append(code);
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override public void onPress(int primaryCode) {}
    @Override public void onRelease(int primaryCode) {}
    @Override public void onText(CharSequence text) {}
    @Override public void swipeLeft() {}
    @Override public void swipeRight() {}
    @Override public void swipeDown() {}
    @Override public void swipeUp() {}
}
