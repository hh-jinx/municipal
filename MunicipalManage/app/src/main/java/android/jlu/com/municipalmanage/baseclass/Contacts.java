package android.jlu.com.municipalmanage.baseclass;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Contacts {
    String name;
    String tel;

    public Contacts(String name, String tel){
        this.name = name;
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
