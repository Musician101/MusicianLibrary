package musician101.luc.gui;

import javax.swing.JTable;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class GuiTable extends JTable
{
	public GuiTable(TableModel model)
	{
		super(model);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
}
