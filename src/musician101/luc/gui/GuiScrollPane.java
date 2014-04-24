package musician101.luc.gui;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class GuiScrollPane extends JScrollPane
{
	public GuiScrollPane(GuiTable table, int x, int y, int w, int h)
	{
		super(table);
		setBounds(x, y, w, h);
	}
}
