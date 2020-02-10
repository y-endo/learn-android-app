package jp.yukiendo.fragmentsample


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */
class MenuThanksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // フラグメントで表示する画面をXMLファイルからインフレートする
        val view = inflater.inflate(R.layout.fragment_menu_thanks, container, false)
        // 所属アクティビティからインテントを取得
        val intent = activity?.intent
        // インテントから引き継ぎデータをまとめたもの(Bundleオブジェクト)を取得
        val extras = intent?.extras
        // 定食名と金額を取得
        val menuName = extras?.getString("menuName")
        val menuPrice = extras?.getString("menuPrice")
        // 定食名と金額を表示させるTextViewを取得
        val tvMenuName = view.findViewById<TextView>(R.id.tvMenuName)
        val tvMenuPrice = view.findViewById<TextView>(R.id.tvMenuPrice)
        // TextViewに定食名と金額を表示
        tvMenuName.text = menuName
        tvMenuPrice.text = menuPrice

        // 戻るボタンを取得
        val btBackButton = view.findViewById<Button>(R.id.btBackButton)
        // 戻るボタンにリスナを登録
        btBackButton.setOnClickListener(ButtonClickListener())

        // インフレートされた画面を戻り値として返す
        return view
    }

    // ボタンが押された時の処理が記述されたメンバクラス
    private inner class ButtonClickListener: View.OnClickListener {
        override fun onClick(view: View) {
            // 自分が所属するアクティビティを終了
            activity?.finish()
        }
    }

}
