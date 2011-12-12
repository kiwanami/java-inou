
import javax.swing.JApplet;
import java.awt.BorderLayout;

public class D3Applet extends JApplet {

	public void init() {
        add(BorderLayout.CENTER, Demo.getDemo());
    }

	/*
	  <applet
	  codebase=".."
	  code="D3Applet.class"
	  width=600 height=550>
	  </applet>
	*/
}
