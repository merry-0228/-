package com.pet.ui;

import com.pet.dao.UserDao;
import com.pet.entity.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JTextField nicknameField;
    private JTextField addressField;
    private JComboBox<String> roleCombo;

    public RegisterFrame() {
        setTitle("宠物服务预约系统 - 注册");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("手机号："));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("密码："));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("昵称："));
        nicknameField = new JTextField();
        panel.add(nicknameField);

        panel.add(new JLabel("地址："));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("身份类型："));
        roleCombo = new JComboBox<>(new String[]{"普通用户", "VIP用户", "商家"});
        panel.add(roleCombo);

        JButton registerBtn = new JButton("注册");
        registerBtn.addActionListener(e -> register());
        panel.add(registerBtn);

        JButton backBtn = new JButton("返回登录");
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        panel.add(backBtn);

        add(panel);
    }

    private void register() {
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String nickname = nicknameField.getText().trim();
        String address = addressField.getText().trim();
        String roleSelect = (String) roleCombo.getSelectedItem();

        // 验证输入
        if (phone.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "手机号、密码、昵称不能为空！");
            return;
        }

        String role;
        switch (roleSelect) {
            case "VIP用户":
                role = "vip";
                break;
            case "商家":
                role = "shop";
                break;
            default:
                role = "user";
                break;
        }

        try {
            UserDao userDao = new UserDao();
            if (userDao.isPhoneExists(phone)) {
                JOptionPane.showMessageDialog(this, "该手机号已注册！");
                return;
            }

            User user = new User(phone, password, nickname, address, role);
            if (userDao.register(user)) {
                JOptionPane.showMessageDialog(this, "注册成功！请登录");
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "注册失败，请重试！");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "注册失败：" + ex.getMessage());
        }
    }
}
