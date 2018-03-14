import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder

class EmailRoute extends RouteBuilder {
  def void configure() {
    println "Creando el RouteBuilder"

    onException(Exception)
    .process({ Exchange exchange ->
        Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        println exception
        println exchange.in.getBody(String)
    })
    .handled(true)

    from("imaps://imap.gmail.com?username=jorge@makingdevs.com"
    + "&password=${System.getenv("PASS")}"
    + "&delete=false"
    + "&consumer.delay=6000")
    .filter{ it.in.headers.subject.contains('camel')}
    .process { Exchange exchange -> 
        println "En camel este es el mensaje"
        String msg = exchange.in.getBody(String)
        println msg
        exchange.out.setBody(msg)
        println "-"*30
    }
    .to("direct:mydirect")
  }
}