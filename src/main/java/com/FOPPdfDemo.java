package com;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

public class FOPPdfDemo {

	public static void main(String[] args) throws ConfigurationException {
		FOPPdfDemo fOPPdfDemo = new FOPPdfDemo();
		try {
			fOPPdfDemo.convertToPDF();
		} catch (FOPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Method that will convert the given XML to PDF
	 * 
	 * @throws IOException
	 * @throws TransformerException
	 * @throws SAXException 
	 * @throws ConfigurationException 
	 */
	public void convertToPDF() throws IOException, TransformerException, SAXException, ConfigurationException {
		
		DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
		// the XSL FO file
		File xsltFile = new File("Resources/loan/KYC_PDF.xsl");
		File configFile = new File("Resources/fop_config.xml");
		
		// the XML file which provides the input
		StreamSource xmlSource = new StreamSource(new File("Resources/loan/kycsample.xml"));

		
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance();
		
		// a user agent is needed for transformation
		
		// Setup output
		OutputStream out;
		out = new java.io.FileOutputStream("Resources/loan/KYCPDF.pdf");

		try {
		//	Configuration cfg = cfgBuilder.build(getClass().getClassLoader().getResourceAsStream(configFile));
			fopFactory.setUserConfig(configFile);
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			// That's where the XML is first transformed to XSL-FO and then
			// PDF is created
			transformer.transform(xmlSource, res);
		} finally {
			out.close();
		}
	}

}
