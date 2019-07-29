package com.lthaoshao.pattern.prototype.entity;

import java.io.*;
import java.util.Date;

/**
 * <p> 齐天大圣 </p>
 *
 * @author lijinghao
 * @version : QiTianDaSheng.java, v 0.1 2019年07月26日 17:15:15 lijinghao Exp $
 */
public class QiTianDaSheng extends Monkey implements Serializable, Cloneable {

    private JinGuBang jinGuBang;

    public QiTianDaSheng() {
        this.birthday = new Date();
        this.jinGuBang = new JinGuBang();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this.deepClone();
    }

    /**
     * 深克隆
     *
     * @return
     */
    private Object deepClone() {

        QiTianDaSheng qiTianDaSheng = null;
        // 通过字节码来克隆
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(this);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(inputStream);
            ) {
                qiTianDaSheng = (QiTianDaSheng) ois.readObject();
                qiTianDaSheng.birthday = new Date();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return qiTianDaSheng;
    }

    public QiTianDaSheng shallowClone(QiTianDaSheng target) {
        QiTianDaSheng qiTianDaSheng = new QiTianDaSheng();
        qiTianDaSheng.height = target.height;
        qiTianDaSheng.weight = target.height;
        qiTianDaSheng.jinGuBang = target.jinGuBang;
        qiTianDaSheng.birthday = new Date();
        return qiTianDaSheng;
    }

    public JinGuBang getJinGuBang() {
        return jinGuBang;
    }

    public void setJinGuBang(JinGuBang jinGuBang) {
        this.jinGuBang = jinGuBang;
    }
}
