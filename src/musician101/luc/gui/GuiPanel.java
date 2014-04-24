package musician101.luc.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GuiPanel extends JPanel
{
	public GuiPanel(int w, int h)
	{
		setPreferredSize(new Dimension(w, h));
	}
}
