package com.app.auth;

import java.io.IOException;
import java.util.List;

import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.model.User;

/**
 * Implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
@Singleton
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// final static String GOOGLE_ENDPOINT =
	// "https://www.google.com/accounts/o8/id";
	private final static String YAHOO_ENDPOINT = "https://me.yahoo.com";

	private Logger l = LoggerFactory.getLogger(LoginServlet.class);
	private ConsumerManager manager;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.manager = new ConsumerManager();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Identifier identifier = this.verifyResponse(req);
		l.debug("identifier: " + identifier);

		if (identifier != null) {
			resp.sendRedirect("/dataservice/subscriptions");
			// resp.sendRedirect("/index.jsp");
		} else {
			l.error("login with openid failed");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String identifier = req.getParameter("identifier");
		this.authRequest(identifier, req, resp);
	}

	// --- placing the authentication request ---
	public String authRequest(String userSuppliedString,
			HttpServletRequest httpReq, HttpServletResponse httpResp)
			throws IOException {
		try {
			// configure the return_to URL where your application will, receive
			// the authentication responses from the OpenID provider
			String returnToUrl = httpReq.getRequestURL().toString();

			// perform discovery on the user-supplied identifier
			List<?> discoveries = manager.discover(userSuppliedString);
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			httpReq.getSession().setAttribute("openid-disc", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

			FetchRequest fetch = FetchRequest.createFetchRequest();
			if (userSuppliedString.startsWith(YAHOO_ENDPOINT)) {
				fetch.addAttribute("email",
						"http://axschema.org/contact/email", true);
				fetch.addAttribute("firstName",
						"http://axschema.org/namePerson/first", true);
				fetch.addAttribute("lastName",
						"http://axschema.org/namePerson/last", true);
			} else { // works for myOpenID
				fetch.addAttribute("fullname",
						"http://schema.openid.net/namePerson", true);
				fetch.addAttribute("email",
						"http://schema.openid.net/contact/email", true);
			}

			// attach the extension to the authentication request
			authReq.addExtension(fetch);
			httpResp.sendRedirect(authReq.getDestinationUrl(true));

		} catch (OpenIDException e) {
			// present error to the user
		}

		return null;
	}

	// --- processing the authentication response ---
	public Identifier verifyResponse(HttpServletRequest httpReq) {
		final User user = new User();

		try {
			// extract the parameters from the authentication response
			// (which comes in as a HTTP request from the OpenID provider)
			ParameterList response = new ParameterList(
					httpReq.getParameterMap());

			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) httpReq
					.getSession().getAttribute("openid-disc");

			// extract the receiving URL from the HTTP request
			StringBuffer receivingURL = httpReq.getRequestURL();
			String queryString = httpReq.getQueryString();
			if (queryString != null && queryString.length() > 0)
				receivingURL.append("?").append(httpReq.getQueryString());

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(
					receivingURL.toString(), response, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification
						.getAuthResponse();

				if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
					FetchResponse fetchResp = (FetchResponse) authSuccess
							.getExtension(AxMessage.OPENID_NS_AX);

					List<?> emails = fetchResp.getAttributeValues("email");
					String email = (String) emails.get(0);
					l.info("OpenIdlogin done with email: " + email);
					l.info("verifier : {}", verified.getIdentifier());

					user.setEmail(email);
					user.setUuid(verified.getIdentifier());
					httpReq.getSession().setAttribute("user", user);
				}

				return verified; // success
			}
		} catch (OpenIDException e) {
			// error
			l.error("open id exception {} ", e.getMessage());
		}

		return null;
	}
}
