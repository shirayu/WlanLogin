package jp.xii.relog.setting;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.*;

/**
 * ファイルリストダイアログクラス
 * @author Iori
 *
 */
public class FileListDialog extends Activity 
	implements View.OnClickListener
			, DialogInterface.OnClickListener
	{

	private Context _parent = null;							//親
	private File[] _dialog_file_list;						//今、表示しているファイルのリスト
	private int _select_count = -1;							//選択したインデックス
	private onFileListDialogListener _listener = null;		//リスナー
	private boolean _is_directory_select = false;			//ディレクトリ選択をするか？
	
	/**
	 * ディレクトリ選択をするか？
	 * @param is
	 */
	public void setDirectorySelect(boolean is){
		_is_directory_select = is;
	}
	public boolean isDirectorySelect(){
		return _is_directory_select;
	}
	
	/**
	 * 選択されたファイル名取得
	 * @return
	 */
	public String getSelectedFileName(){
		String ret = "";
		if(_select_count < 0){
		
		}else{
			ret = _dialog_file_list[_select_count].getName();
		}
		return ret;
	}
	
	/**
	 * ファイル選択ダイアログ
	 * @param context 親
	 */
	public FileListDialog(Context context){
		_parent = context;
	}
	
	@Override
	public void onClick(View v) {
		// 処理なし		
	}

	/**
	 * ダイアログの選択イベント
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		//選択されたので位置を保存
		_select_count = which;
		if((_dialog_file_list == null) || (_listener == null)){
		}else{
			File file = _dialog_file_list[which];
			
//			Util.outputDebugLog("getAbsolutePath : " + file.getAbsolutePath());
//			Util.outputDebugLog("getPath : " + file.getPath());
//			Util.outputDebugLog("getName : " + file.getName());
//			Util.outputDebugLog("getParent : " + file.getParent());
			
			if(file.isDirectory() && !isDirectorySelect()){
				//選択した項目がディレクトリで、ディレクトリ選択しない場合はもう一度リスト表示
				show(file.getAbsolutePath(), file.getPath());
			}else{
				//それ以外は終了なので親のハンドラ呼び出す
				_listener.onClickFileList(file);
			}
		}
	}

	/**
	 * ダイアログ表示
	 * @param context 親
	 * @param path 表示したいディレクトリ
	 * @param title ダイアログのタイトル
	 */
	public void show(String path, String title){
		
		try{
			_dialog_file_list = new File(path).listFiles();
			if(_dialog_file_list == null){
				//NG
				if(_listener != null){
					//リスナーが登録されてたら空で呼び出す
					_listener.onClickFileList(null);
				}
			}else{
				String[] list = new String[_dialog_file_list.length];
				int count = 0;
				String name = "";

				//ファイル名のリストを作る
				for (File file : _dialog_file_list) {
					if(file.isDirectory()){
						//ディレクトリの場合
						name = file.getName() + "/";
					}else{
						//通常のファイル
						name = file.getName();
					}
					list[count] = name;
					count++;
				}

				//ダイアログ表示
				new AlertDialog.Builder(_parent).setTitle(title).setItems(list, this).show();
			}
		}catch(SecurityException se){
			//Util.outputDebugLog(se.getMessage());
		}catch(Exception e){
			//Util.outputDebugLog(e.getMessage());
		}
		
	}
	
	/**
	 * リスナーのセット
	 * @param listener
	 */
	public void setOnFileListDialogListener(onFileListDialogListener listener){
		_listener = listener;
	}
	
	/**
	 * クリックイベントのインターフェースクラス
	 * @author Iori
	 *
	 */
	public interface onFileListDialogListener{
		public void onClickFileList(File file);
	}

}

