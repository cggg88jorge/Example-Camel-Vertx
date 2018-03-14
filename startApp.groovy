@Grab(group='org.apache.camel', module='camel-core', version='2.20.2')
@Grab(group='org.apache.camel', module='camel-mail', version='2.20.2')
@Grab(group='io.vertx', module='vertx-core', version='3.4.1')
@Grab(group='io.vertx', module='vertx-camel-bridge', version='3.4.1')

import io.vertx.core.Vertx

Vertx vertx = Vertx.vertx()

import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import io.vertx.camel.CamelBridge
import io.vertx.camel.CamelBridgeOptions
import io.vertx.camel.InboundMapping
import io.vertx.camel.OutboundMapping

CamelContext camelContext = new DefaultCamelContext()
println "Init the camel context"

vertx.eventBus().consumer( "com.makingdevs.vertx" ){ message ->
  println "En el verticle de vertx"
  println message.receivedBody
}

camelContext.addRoutes(new EmailRoute())

CamelBridge.create(vertx,
    new CamelBridgeOptions(camelContext)
      .addInboundMapping(InboundMapping.fromCamel("direct:mydirect").toVertx("com.makingdevs.vertx"))
      //.addOutboundMapping(OutboundMapping.fromVertx("com.makingdevs.vertx").toCamel("direct:vertx"))
).start();

camelContext.start()