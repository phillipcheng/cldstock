package org.cldutil.stock.chart;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;

import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;

import org.cldutil.stock.common.StockDataConfig;
import org.cldutil.stock.strategy.IntervalUnit;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class DataConfigPanel extends JPanel {

	protected static Logger logger =  LogManager.getLogger(DataConfigPanel.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat msdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final long serialVersionUID = 1L;

	private JLabel lblStockId;
	private JTextField txtStockId;
	private JLabel lblInterval;
	private JComboBox cbUnit = null;
	private JLabel lblStart;
	private JTextField tfStartDt = null;
	private JLabel lblEnd;
	private JTextField tfEndDt = null;
	private JTextField txtBaseMarketId;
	private JFileChooser strategyFileChooser = new JFileChooser();
	private JButton btnChooseStrategy = new JButton("Choose");
	
	private StockDataConfig dc;  //  @jve:decl-index=0:
	private String strategyFile;
	private MainFrame mf;
	
	/**
	 * This is the default constructor
	 */
	public DataConfigPanel() {
		super();
		initialize();
	}
	
	public void setDataConfig(StockDataConfig dc){
		txtStockId.setText(dc.getStockId());
		txtBaseMarketId.setText(dc.getBaseMarketId());
		cbUnit.setSelectedItem(dc.getUnit());
		if (dc.getStartDt()!=null){
			if (dc.getUnit()==IntervalUnit.day){
				tfStartDt.setText(sdf.format(dc.getStartDt()));
				tfEndDt.setText(sdf.format(dc.getEndDt()));
			}else{
				tfStartDt.setText(msdf.format(dc.getStartDt()));
				tfEndDt.setText(msdf.format(dc.getEndDt()));
			}
		}
	}
	
	public StockDataConfig getDataConfig() {
		dc = new StockDataConfig();
		dc.setStockId(txtStockId.getText());
		dc.setBaseMarketId(txtBaseMarketId.getText());
		dc.setUnit((IntervalUnit) cbUnit.getSelectedItem());
		try {
			if (dc.getUnit()==IntervalUnit.day){
				dc.setStartDt(sdf.parse(tfStartDt.getText()));
				dc.setEndDt(sdf.parse(tfEndDt.getText()));
			}else{
				String strStartDt = tfStartDt.getText();
				String strEndDt = tfEndDt.getText();
				if (!strStartDt.contains(":")){
					strStartDt = strStartDt + " 09:30";
					strEndDt = strEndDt + " 16:00";
					tfStartDt.setText(strStartDt);
					tfEndDt.setText(strEndDt);
				}
				dc.setStartDt(msdf.parse(strStartDt));
				dc.setEndDt(msdf.parse(strEndDt));
			}
		}catch(Exception e){
			logger.error("", e);
		}
		
		return dc;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		lblInterval = new JLabel();
		lblInterval.setBounds(new Rectangle(10, 100, 60, 25));
		lblInterval.setText("Unit:");
		lblEnd = new JLabel();
		lblEnd.setBounds(new Rectangle(10, 151, 46, 22));
		lblEnd.setText("EndDt:");
		lblStart = new JLabel();
		lblStart.setBounds(new Rectangle(10, 127, 46, 22));
		lblStart.setText("StartDt:");
		lblStockId = new JLabel();
		lblStockId.setText("StockId:");
		lblStockId.setBounds(new Rectangle(10, 73, 60, 25));
		this.setSize(221, 184);
		this.setLayout(null);
		this.add(lblStockId, null);
		this.add(lblStart, null);
		this.add(getTfStartDt(), null);
		this.add(lblEnd, null);
		this.add(getTfEndDt(), null);
		this.add(lblInterval, null);
		this.add(getCbUnit(), null);
		
		txtStockId = new JTextField();
		txtStockId.setBounds(91, 73, 86, 20);
		add(txtStockId);
		txtStockId.setColumns(10);
		
		JLabel lblBaseMarketId = new JLabel();
		lblBaseMarketId.setText("BaseMarketId:");
		lblBaseMarketId.setBounds(new Rectangle(0, 34, 60, 25));
		lblBaseMarketId.setBounds(10, 50, 70, 25);
		add(lblBaseMarketId);
		
		txtBaseMarketId = new JTextField();
		txtBaseMarketId.setColumns(10);
		txtBaseMarketId.setBounds(91, 50, 86, 20);
		add(txtBaseMarketId);

		btnChooseStrategy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnChooseStrategy) {
					int returnVal = strategyFileChooser.showOpenDialog(DataConfigPanel.this);
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			        	File file = strategyFileChooser.getSelectedFile();
			        	try {
			        		strategyFile = file.getCanonicalPath();
							logger.info("Opening: " + strategyFile);
							btnChooseStrategy.setText(file.getName());
							mf.setStrategy(strategyFile);
						}catch (IOException e1) {
							logger.error("", e1);
						}
			        }else{
			        	logger.info("Open command cancelled by user.");
			        }
			    }
			}
		});
		btnChooseStrategy.setBounds(10, 21, 204, 23);
		add(btnChooseStrategy);
	}

	/**
	 * This method initializes tfSD	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfStartDt() {
		if (tfStartDt == null) {
			tfStartDt = new JTextField();
			tfStartDt.setBounds(new Rectangle(91, 125, 123, 22));
			tfStartDt.setName("tfType");
		}
		return tfStartDt;
	}

	/**
	 * This method initializes tfED	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfEndDt() {
		if (tfEndDt == null) {
			tfEndDt = new JTextField();
			tfEndDt.setBounds(new Rectangle(91, 149, 123, 22));
			tfEndDt.setName("tfType");
		}
		return tfEndDt;
	}

	/**
	 * This method initializes cbIndustry	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbUnit() {
		if (cbUnit == null) {
			cbUnit = new JComboBox();
			cbUnit.setModel(new DefaultComboBoxModel(IntervalUnit.values()));
			cbUnit.setBounds(new Rectangle(91, 98, 116, 25));
		}
		return cbUnit;
	}

	public String getStrategyFile() {
		return strategyFile;
	}

	public void setStrategyFile(String strategyFile) {
		this.strategyFile = strategyFile;
	}

	public JFileChooser getStrategyFileChooser() {
		return strategyFileChooser;
	}

	public void setStrategyFileChooser(JFileChooser strategyFileChooser) {
		this.strategyFileChooser = strategyFileChooser;
	}

	public JButton getBtnChooseStrategy() {
		return btnChooseStrategy;
	}

	public void setBtnChooseStrategy(JButton btnChooseStrategy) {
		this.btnChooseStrategy = btnChooseStrategy;
	}

	public MainFrame getMf() {
		return mf;
	}

	public void setMf(MainFrame mf) {
		this.mf = mf;
	}
}  //  @jve:decl-index=0:visual-constraint="4,5"
