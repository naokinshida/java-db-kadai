package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Posts_Chapter07 {
    public static void main(String[] args) {
        // データベース接続用の変数を宣言
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        // 投稿データの配列
        String[][] postsList = {
            {"1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13"},
            {"1002", "2023-02-08", "お疲れ様です！", "12"},
            {"1003", "2023-02-09", "今日も頑張ります！", "18"},
            {"1001", "2023-02-09", "無理は禁物ですよ！", "17"},
            {"1002", "2023-02-10", "明日から連休ですね！", "20"}
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "Gtrnsx_34"
            );
            System.out.println("データベース接続成功");

            // SQLクエリを実行するためのStatementを作成
            statement = con.createStatement();

            // レコード追加処理
            StringBuilder insertSql = new StringBuilder("INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES ");
            for (int i=0; i < postsList.length; i++) {
                String[] post = postsList[i];
                insertSql.append(String.format(
                    "(%s, '%s', '%s', %s)", post[0], post[1], post[2], post[3]
                ));
                if (i < postsList.length - 1) {
                    insertSql.append(", "); // 最後の要素でなければカンマを追加
                }
            }
            statement.executeUpdate(insertSql.toString());

            System.out.println("レコード追加を実行します");
            System.out.println(postsList.length + "件のレコードが追加されました");

            // user_idの値を変数で定義
            int userId = 1002;

            // ユーザーIDが変数userIdのレコードを検索することをメッセージで表示
            System.out.println("ユーザーIDが" + userId + "のレコードを検索しました");

            // データを検索して表示
            String selectSql = "SELECT * FROM posts WHERE user_id = " + userId;
            resultSet = statement.executeQuery(selectSql);

            int rowCounter = 1; // 件数
            while (resultSet.next()) {
                // getDateメソッドを使って日付型のデータを取得
                java.sql.Date posted_at = resultSet.getDate("posted_at");
                String post_content = resultSet.getString("post_content");
                int likes = resultSet.getInt("likes");
                System.out.println(rowCounter + "件目：投稿日時=" + posted_at +
                    "／投稿内容=" + post_content + "／いいね数=" + likes);
                rowCounter++;
            }
        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
            e.printStackTrace();
        } finally {
            // 使用したオブジェクトを解放
            if (resultSet != null) {
                try { resultSet.close(); } catch (SQLException ignore) {}
            }
            if (statement != null) {
                try { statement.close(); } catch (SQLException ignore) {}
            }
            if (con != null) {
                try { con.close(); } catch (SQLException ignore) {}
            }
        }
    }
}
