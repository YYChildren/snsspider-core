package com.mingchao.snsspider.login.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mingchao.snsspider.login.LoginParam;

@SuppressWarnings("serial")
public class LoginParamView extends JFrame implements LoginParam {

	private final JPanel contentPanel = new JPanel();
	private Map<String, JTextComponent> keyFileds = new HashMap<String, JTextComponent>();
	private Set<LoginParamListener> tlsSet = new HashSet<LoginParamListener>();
	private Map<String, String> kv;

	/**
	 * Create the dialog.
	 */
	public LoginParamView() {
		addTintListener(new LoginParamListener() {
			public void action(LoginParamEvent event) {
				if(event.getCmd() == LoginParamEvent.TintCmd.CLOSE){
					close();
				}else if(event.getCmd() == LoginParamEvent.TintCmd.CONTINUE){
					submit();
				}
			}
		});

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

			getContentPane().add(buttonPane, BorderLayout.SOUTH);
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
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	protected void doOK() {
		kv = new HashMap<String, String>();
		for (Iterator<Entry<String, JTextComponent>> iterator = keyFileds
				.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, JTextComponent> en = iterator.next();
			String key = en.getKey();
			String value = en.getValue().getText().trim();
			kv.put(key, value);
		}
		synchronized (this) {
			setVisible(false);
			notifyAll();
		}
	}

	private void close() {
		dispose();
	}

	public void setTint(String key, String tint) {
		{
			JPanel subPane = new JPanel();
			subPane.setLayout(new FlowLayout());
			contentPanel.add(subPane);
			JLabel tintLabel = new JLabel(tint);
			subPane.add(tintLabel);
			JTextField valueField;
			valueField = new JTextField();
			subPane.add(valueField);
			valueField.setColumns(20);
			keyFileds.put(key, valueField);
		}
	}

	public void setTint(String key, String tint, boolean hidden) {
		{
			JPanel subPane = new JPanel();
			subPane.setLayout(new FlowLayout());
			contentPanel.add(subPane);
			JLabel tintLabel = new JLabel(tint);
			subPane.add(tintLabel);
			JPasswordField valueField = new JPasswordField();
			subPane.add(valueField);
			valueField.setColumns(20);
			keyFileds.put(key, valueField);
		}
	}

	public void setTint(String key, String tint, Object extInfo) {
		{
			JPanel subPane = new JPanel();
			subPane.setLayout(new FlowLayout());
			contentPanel.add(subPane);
			JLabel tintLabel = new JLabel(tint);
			subPane.add(tintLabel);
			JTextField valueField;
			valueField = new JTextField();
			subPane.add(valueField);
			valueField.setColumns(20);
			keyFileds.put(key, valueField);
			JLabel image = new JLabel(new ImageIcon((byte[]) extInfo));
			subPane.add(image);
		}
	}

	public void submit() {
		setVisible(true);
	}

	public void retry() {
		notifyTintListeners(new LoginParamEvent(this, LoginParamEvent.TintCmd.CONTINUE));
	}

	public void finish() {
		notifyTintListeners(new LoginParamEvent(this, LoginParamEvent.TintCmd.CLOSE));
	}

	public Map<String, String> getLoginInfo() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return kv;
		}
	}

	public void addTintListener(LoginParamListener tls) {
		tlsSet.add(tls);
	}

	public void removeTintListener(LoginParamListener tls) {
		tlsSet.remove(tls);
	}

	public void notifyTintListeners(LoginParamEvent event) {
		for (Iterator<LoginParamListener> iterator = tlsSet.iterator(); iterator
				.hasNext();) {
			LoginParamListener loginParamListener = iterator.next();
			loginParamListener.action(event);
		}
	}
}
