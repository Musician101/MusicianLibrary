package musician101.luc.gui;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

public class Gui
{
	@SuppressWarnings({ "serial", "rawtypes" })
	public static class ComboBox extends JComboBox
	{
		@SuppressWarnings("unchecked")
		public ComboBox(Object[] items, int x, int y, int w, int h)
		{
			super(items);
			setBounds(x, y, w, h);
		}
		
		public void addAll(Object[] items)
		{
			addAll(Arrays.asList(items));
		}
		
		@SuppressWarnings("unchecked")
		public void addAll(List<Object> items)
		{
			for (Object item : items)
				addItem(item);
		}
	}
	
	@SuppressWarnings("serial")
	public static class Panel extends JPanel
	{
		public Panel(int w, int h)
		{
			setPreferredSize(new Dimension(w, h));
		}
	}
	
	@SuppressWarnings("serial")
	public static class ScrollPane extends JScrollPane
	{
		public ScrollPane(Table table, int x, int y, int w, int h)
		{
			super(table);
			setBounds(x, y, w, h);
		}
	}
	
	@SuppressWarnings("serial")
	public static class Table extends JTable
	{
		public Table(TableModel model)
		{
			super(model);
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
	}
	
	@SuppressWarnings("serial")
	public static class TextField extends JTextField
	{
		public TextField(int x, int y, int w, int h)
		{
			setBounds(x, y, w, h);
		}
	}
}
