import java.awt.BorderLayout;
import javax.swing.JApplet;


public class D3DemoApplet extends JApplet {

	public void init() {
        add(BorderLayout.CENTER, D3Demo.getDemo());
    }

	/*
	  <applet
	  codebase=".."
	  code="D3DemoApplet.class"
	  width=600 height=550>
	  </applet>
	*/
}
