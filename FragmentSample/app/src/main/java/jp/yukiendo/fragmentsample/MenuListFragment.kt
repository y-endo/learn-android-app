package jp.yukiendo.fragmentsample


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter

/**
 * A simple [Fragment] subclass.
 */
class MenuListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // フラグメントで表示する画面をXMLファイルからインフレートする
        val view = inflater.inflate(R.layout.fragment_menu_list, container, false)
        // 画面部品ListViewを取得
        val lvMenu = view.findViewById<ListView>(R.id.lvMenu)

        // SimpleAdapterで使用するMutableListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()

        // Mapオブジェクトの用意とmenuListへのデータ登録
        var menu = mutableMapOf("name" to "ハンバーグ定食", "price" to "800円")
        menuList.add(menu)
        menu = mutableMapOf("name" to "からあげ定食", "price" to "800円")
        menuList.add(menu)
        menu = mutableMapOf("name" to "野菜炒め定食", "price" to "800円")
        menuList.add(menu)

        // SimpleAdapter第4引数from用データの用意
        val from = arrayOf("name", "price")
        // SimpleAdapter第5引数to用データの用意
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        // SimpleAdapterを生成
        val adapter = SimpleAdapter(activity, menuList, android.R.layout.simple_expandable_list_item_2, from, to)
        // アダプタの登録
        lvMenu.adapter = adapter

        // リスナの登録
        lvMenu.onItemClickListener = ListItemClickListener()

        // インフレートされた画面を戻り値として返す
        return view
    }

    // リストがタップされた時の処理が記述されたメンバクラス
    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行のデータを取得。SimpleAdapterでは1行分のデータはMutableMap型
            val item = parent.getItemAtPosition(position) as MutableMap<String, String>
            // 定食名と金額を取得
            val menuName = item["name"]
            val menuPrice = item["price"]
            // インテントオブジェクトを生成
            val intent = Intent(activity, MenuThanksActivity::class.java)
            // 第2画面に送るデータを格納
            intent.putExtra("menuName", menuName)
            intent.putExtra("menuPrice", menuPrice)
            // 第2画面の起動
            startActivity(intent)
        }
    }

}
