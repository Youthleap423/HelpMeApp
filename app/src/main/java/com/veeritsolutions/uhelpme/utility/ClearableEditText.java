package com.veeritsolutions.uhelpme.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ${hitesh} on 3/8/2017.
 */

public class ClearableEditText extends AppCompatEditText implements View.OnTouchListener,
        View.OnFocusChangeListener {
    Context context;
    private Drawable xD;
    private Listener listener;
    private OnTouchListener l;
    private OnFocusChangeListener f;
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (isFocused()) {
                setClearIconVisible(isNotEmpty(s.toString()));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };
    private EditTextImeBackListener mOnImeBack;

    public ClearableEditText(Context context) {
        super(context);
        init(context);

    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

	/*@Override
    public void onTextChanged(EditText dataView, String text) {
		if (isFocused()) {
			setClearIconVisible(isNotEmpty(text));
		}
	}*/

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    private void init(Context context) {

        this.context = context;

        xD = getCompoundDrawables()[2];
//		if (xD == null) {
//			xD = getResources().getDrawable(android.R.drawable.presence_offline);
//		}
        if (xD != null) {
            xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        }
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
//		addTextChangedListener(new TextWatcherAdapter(this, this));
        addTextChangedListener(mTextWatcher);
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack.onImeBack(this, this.getText().toString());
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        mOnImeBack = listener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Just ignore the [Enter] key
        return keyCode == KeyEvent.KEYCODE_ENTER || super.onKeyDown(keyCode, event);
    }

    public interface Listener {
        void didClearText();
    }

    public interface EditTextImeBackListener {
        void onImeBack(ClearableEditText ctrl, String text);
    }
}

