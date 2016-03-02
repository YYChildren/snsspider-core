package com.mingchao.snsspider.login.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;

import com.mingchao.snsspider.login.LoginCookieParam;

@SuppressWarnings("serial")
public class LoginCookieView extends JFrame implements LoginCookieParam {

	private JTextArea cookieArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginCookieView dialog = new LoginCookieView();
			dialog.submit();
			String cookie = dialog.getCookiesString();
			System.out.println(new JSONArray(cookie));
			dialog.retry();
			cookie = dialog.getCookiesString();
			System.out.println(new JSONArray(cookie));
			dialog.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LoginCookieView() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{// NORTH
			JPanel tintPane = new JPanel();
			getContentPane().add(tintPane, BorderLayout.NORTH);
			{
				JLabel lblcookiejson = new JLabel("请输入Cookie（JSON格式）：");
				tintPane.add(lblcookiejson);
			}
		}
		{// CENTER
			JScrollPane contentPane = new JScrollPane();
			getContentPane().add(contentPane, BorderLayout.CENTER);
			{
				cookieArea = new JTextArea();
				cookieArea.setLineWrap(true);
				cookieArea.setWrapStyleWord(true);
				contentPane.setViewportView(cookieArea);
			}

			// 分别设置水平和垂直滚动条自动出现
			contentPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			contentPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		{// SOUTH
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				{
					JButton okButton = new JButton("OK");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							doOK();
						}
					});
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}

				{
					JButton button = new JButton("Cancel");
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							doCancel();
						}
					});
					button.setActionCommand("Cancel");
					buttonPane.add(button);
				}
			}
		}

	}

	protected void doCancel() {
		LogFactory.getLog(this.getClass()).info("Cancel");
		System.exit(DO_NOTHING_ON_CLOSE);
	}

	protected void doOK() {
		synchronized (this) {
			setVisible(false);
			notifyAll();
		}
	}

	@Override
	public String getCookiesString() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String cookies = cookieArea.getText().trim();
			return cookies.equals("") ? null : cookies;
		}
	}

	@Override
	public void submit() {
		setVisible(true);
	}

	@Override
	public void retry() {
		cookieArea.setText("");
		setVisible(true);
	}

	@Override
	public void finish() {
		dispose();
	}

}
