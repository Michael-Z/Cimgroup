package cn.com.cimgroup.view;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 禁止表情输入框
 * @author 秋风
 *
 */
@SuppressLint("NewApi") 
public class ProhibitExpressionEditText extends EditText implements MenuItem.OnMenuItemClickListener {
	private static final int ID_SELECTION_MODE = android.R.id.selectTextMode;
    private static final int ID_SELECT_ALL = android.R.id.selectAll;
    private static final int ID_CUT = android.R.id.cut;
    private static final int ID_COPY = android.R.id.copy;
    private static final int ID_PASTE = android.R.id.paste;
    
	private final Context mContext;

	/*
	 * Just the constructors to create a new EditText...
	 */
	public ProhibitExpressionEditText(Context context) {
		super(context);
		this.mContext = context;
	}

	public ProhibitExpressionEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public ProhibitExpressionEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
//		menu.add(0, ID_PASTE, 0, "粘贴").setOnMenuItemClickListener(this);
//		menu.add(0, ID_CUT, 1, "剪切").setOnMenuItemClickListener(this);
//		menu.add(0, ID_COPY, 1, "复制").setOnMenuItemClickListener(this);
//		menu.add(0, ID_SELECT_ALL, 1, "全选").setOnMenuItemClickListener(this);
		super.onCreateContextMenu(menu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		return onTextContextMenuItem(item.getItemId());
	}

	@Override
	public boolean onTextContextMenuItem(int id) {
		
		// Do your thing:
		boolean consumed = super.onTextContextMenuItem(id);
		// React:
		switch (id) {
		case android.R.id.cut:
			onTextCut();
			break;
		case android.R.id.paste:
			onTextPaste();
			break;
		case android.R.id.copy:
			onTextCopy();
		}
		return consumed;
	}

	/**
	 * Text was cut from this EditText.
	 */
	public void onTextCut() {
		Toast.makeText(mContext, "Cut!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Text was copied from this EditText.
	 */
	public void onTextCopy() {
		Toast.makeText(mContext, "Copy!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Text was pasted into the EditText.
	 */
	public void onTextPaste() {
		ClipboardManager clip = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
		insert(getFace(clip.getText().toString()));
	}
	
	/**
	 * 向输入框里添加表情
	 * */
	private void insert(CharSequence text) {
		int iCursorStart = Selection.getSelectionStart( getText())-text.toString().length() ;
		int iCursorEnd = Selection.getSelectionEnd( getText() );
		if (iCursorStart != iCursorEnd) {
			((Editable) getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd(( getText()));
		((Editable) getText()).insert(iCursor, text);
	}
	
	private SpannableStringBuilder getFace(String textStr) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			String pngBefore = textStr.substring(0 , textStr.indexOf("#" ) )  ;
			String png =  textStr.substring(textStr.indexOf("[")+1, textStr.indexOf("]" ))  ;
			String pngBehind =  textStr.substring(textStr.indexOf("#",textStr.indexOf("#"))+1 , textStr.length())  ;
			sb.append(textStr);
			sb.setSpan(
					new ImageSpan(getContext() ,  BitmapFactory.decodeStream(getContext().getAssets().open(png))), textStr.indexOf("#" ) , textStr.indexOf("#",textStr.indexOf("#")+1)+1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}
}
