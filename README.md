# PaaS Unified Library #

The PaaS Unified Library is a layer of abstraction over the libraries provided by PaaS platforms to manage typical operations: 

* create application
* delete application
* start application
* stop application
* create database
* delete database
* dump database
* restore database

It was extracted from Cloud4SOA project.

### Components ###

The library is comprised of the following components:

* api: interface to the client programmer.
* api implementer (ems): orchestrator; forwards the operations to local or remote adapters, accordingly.
* remote adapters: Remote adapters are webapps with a REST interface that are in charge of executing the operations for a single PaaS. There are remote adapters for CloudBees and CloudFoundry.
* local adapter: some operations (deploy, dump & restore database) can't be done in a remote adapter, where there is no confidence of having full control over the system.

### Differences with Cloud4SOA ###

From the adapter point of view, Cloud4SOA deploys one Remote Adapter for each application to deploy in a PaaS provider.

So, when deploying an application, the first step is to deploy the RA, and after this, the application. One of the benefits of the RA is that it provides some kind of monitoring (like response times) without changing the application. This way, the application was C4S agnostic. 

This architecture is a bit tricky because, what adapter can we use to deploy the RA? In C4S, the Local Adapter was implemented to do the things that the RA couldn’t do, like deploying an adapter, or executing a mysql client.

This library gives more flexibility, as one of the parameters for operation is the adapter url. This allows the following architectures:

* 1 RA deployed per application (C4S approach)
* 1 RA deployed per PaaS provider: the single RA could be deployed locally or remotely (must be deployed previously)

### TODO ###

The remote adapters provide a clean interface, but they need excessive work to use compared with a direct call to a paas-provided-library.

If the code in remote adapters is decoupled from the REST layer, it could be called directly from the EMS. This way, the adapter url parameter makes no sense: a quick and dirty fix is to execute the "local" code if the adapter url is empty.

### Install ###

    cd $root
    mvn install -f parent/pom.xml -Dmaven.test.skip=true
    mvn install -f RemoteAdapters/CloudBeesAdapter/pom.xml
    mvn install -f RemoteAdapters/CloudFoundryC4SAdapter.xml
    cp governance-ems/src/test/resources/conf/credentials.properties.sample \
        governance-ems/src/test/resources/conf/credentials.properties

This should compile all subprojects.

We are skipping tests because it needs the requested adapter up.

### Testing ###
We will execute governance-ems tests

The first step is to place the right credentials in ./governance-ems/src/test/resources/conf/credentials.properties

Check in setUp() method the PaaS to use (paasInfo variable), and run the proper RemoteAdapter.

If CloudBees:

    mvn tomcat:run -f RemoteAdapters/CloudBeesAdapter/pom.xml

If CloudFoundry:

    mvn tomcat:run -f RemoteAdapters/CloudFoundryC4SAdapter/pom.xml

Launch test:

    mvn test -f governance-ems/pom.xml
