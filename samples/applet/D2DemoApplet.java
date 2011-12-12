
import java.awt.BorderLayout;
import javax.swing.JApplet;

public class D2DemoApplet extends JApplet {

	public void init() {
        add(BorderLayout.CENTER, D2Demo.getDemo());
    }

	/*
	  <applet
	  codebase=".."
	  code="D2DemoApplet.class"
	  width=600 height=550>
	  </applet>
	*/
}
