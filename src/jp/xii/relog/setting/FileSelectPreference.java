package jp.xii.relog.setting;

import java.io.File;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import jp.xii.relog.setting.FileListDialog;
import jp.xii.relog.setting.FileListDialog.onFileListDialogListener;


/**
 * ファイルリストプリファレンス
 * @author Iori
 *
 * 拡張パラメータ
 * rootPath : 表示開始するパス（フルパス）
 */
public class FileSelectPreference extends DialogPreference
	implements FileListDialog.onFileListDialogListener{

	private String _rootPath = "/";			//ルートパス
	private String _defaultPath = "";		//ファイルリストで選択したパス
	
	/**
	 * コンストラクタ
	 * @param context
	 * @param attrs
	 */
	public FileSelectPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//デフォルトバリュー取得
		String default_value = attrs.getAttributeValue(null, "defaultValue");
		if(default_value == null){
			_defaultPath = "/";
		}else{
			_defaultPath = default_value;
		}
	}

	/**
	 * 表示したときに呼ばれる
	 */
	@Override
	protected void onBindView(View view) {
		//設定を読み込み
		SharedPreferences pref = getSharedPreferences();
		if(pref == null){
		}else{
			_defaultPath = pref.getString(getKey(),  _defaultPath);
		}

		//サマリーに現在値を設定
		setSummary((CharSequence) _defaultPath);

		//これはなぜか最後じゃないとイケないらしい
		super.onBindView(view);
	}

	/**
	 * プリファレンスのクリックイベント
	 */
	@Override
	protected void onClick(){
		//どこかのonClickででもこんな呼び出しをする
		FileListDialog dlg = new FileListDialog(getContext());
		//リスナーの登録
		dlg.setOnFileListDialogListener((onFileListDialogListener) this);
		//ディレクトリを選択するか
		//dlg.setDirectorySelect(true);
		//表示（とりあえずルートから）
		dlg.show( _rootPath, _rootPath);
		
	}


	/**
	 * ファイルの選択結果のイベント
	 */
	@Override
	public void onClickFileList(File file) {
		
		if(file == null){
			Toast.makeText(getContext(), "ファイルの取得ができませんでした", Toast.LENGTH_SHORT).show();
		}else{
			//確認ダイアログ表示
			setDialogTitle((CharSequence)"確認");
			setDialogMessage((CharSequence) file.getAbsolutePath());
			showDialog(null);
		}
	}
	

	/**
	 * 確認ダイアログが閉じた時のイベント
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if(!positiveResult){
		}else{
			//設定を保存
			SharedPreferences.Editor editor = getEditor();
			editor.putString(getKey(), (String) getDialogMessage());
			editor.commit();

			//サマリーを更新
			notifyChanged();
		}
	}
	
}
