package it.gammainnovation.wave25.webMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.gammainnovation.wave25.actions.ActionInterface;
import it.gammainnovation.wave25.config.MenuConfigEngine;
import it.gammainnovation.wave25.menu.MenuEngine;
import it.gammainnovation.wave25.menu.MenuException;

@WebServlet("/TopMenu")
public class TopMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TopMenu() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MenuEngine me = null;
		String path = "C:/Users/azzol/eclipse-workspace/MenuProject/src/it/gammainnovation/wave25/menu_anagrafica.txt";
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();

		if (session.isNew()) {
			try {
				MenuConfigEngine mce = new MenuConfigEngine();
				me = mce.configMenu(path);
				session.putValue("MenuEngine", me);
				
			} catch (Exception e) {
				out.println("<html><body><h2>IMPOSSIBILE CARICARE IL MENU'</h2></body></head>");
				out.close();
				return;
			}
		}
		me = (MenuEngine) session.getAttribute("MenuEngine");
		
		out.println("<html><head><title>Menu</title><link rel='stylesheet' type='text/css' href='CSS/Style.css'></head>");
		out.println("<body><table><tr><th class='image'></th><th class='options'><h2>Menù Anagrafica</h2><br>");
		out.println("<form action 'http://localhost:8080/MenuWeb/TopMenu' method='POST'>");
		try {			
			Set<Entry<String, ActionInterface>> set = me.getItems();
			for (Entry<String, ActionInterface> entry : set) {
				//out.println("<br><p>" + entry.getKey() + ") " + entry.getValue().getDescription() + "</p>");
				out.println("<input type= 'radio' name='value' value='"+entry.getKey()+"'>");
				out.println("<label>"+entry.getValue().getDescription()+"</label><br>");
			}
		} catch (MenuException e) {
			e.printStackTrace();
		}
		
		//out.println("<br><input type= 'text' name='value' required><br>");
		out.println("<br><input type= 'submit' value='vai'></form></th></tr></table></body></html>");
		out.close();
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Enumeration<String> values = request.getParameterNames();

		HttpSession session = request.getSession();
		
		if (session.isNew()) {
			out.println("<h1>ERRORE: SESSIONE NON TROVATA</h1>");
		}
		
		MenuEngine me = (MenuEngine) session.getAttribute("MenuEngine");
		
		out.println("<html><head><title>Menu</title><link rel='stylesheet' type='text/css' href='CSS/Style.css'></head>");

		while (values.hasMoreElements()) {
			String name = (String) values.nextElement();
			String[] value = request.getParameterValues(name);
			
			for (int i = 0; i < value.length; i++) {
				
				if (name.compareTo("submit") != 0) {
					
					try {
						
						Set<Entry<String, ActionInterface>> set = me.postItems((String) value[i]);
						out.println("<body><table><tr><th class='image'></th><th class='options'><h2>" + me.getCurrentMenu().getName() +"</h2><br>");
						out.println("<form action 'http://localhost:8080/MenuWeb/TopMenu' method='POST'>");
						for (Entry<String, ActionInterface> entry : set) {
							//out.println("<br><p>" + entry.getKey() + ") " + entry.getValue().getDescription() + "</p>");
							out.println("<input type= 'radio' name='value' value='"+entry.getKey()+"'>");
							out.println("<label>"+entry.getValue().getDescription()+"</label><br>");
						}
					} catch (MenuException e) {
					
						e.printStackTrace();
					}
					
				}
			}
			//out.println("<br><input type= 'text' name='value' required><br>");
			out.println("<br><input type= 'submit' value='vai'></form></th></tr></table></body></html>");
			out.close();
		}

	}

}
