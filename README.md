# Sentry Release Maven Plugin

In the Sentry releases documentation, version are set using the commit hash, which could be disturbing for a
monitoring team. This plugin use the Maven pom.xml version set in the project instead. A release is always created on
Sentry, without the 'SNAPSHOT' suffix. 

Sentry Release Maven Plugin allow to create and deploy a release into [Sentry](https://sentry.io/welcome/) monitoring tool.
The aim of this plugin is to create and deploy release following the Maven conventions, while providing Git commits
via the Sentry API. This plugin does not work with other SCM. 

This plugin has been developed for Sentry 9.9.3.

## Quick start

### Configure the plugin
Add into your Maven project, into the `<build>` section, a plugin


```xml
<plugin>
    <groupId>com.jlefebure</groupId>
    <artifactId>sentry-release-maven-plugin</artifactId>
    <version>9.9.3.3</version>
    
    <configuration>
        <sentryUrl>https://yoursentryinstance.com/api/0</sentryUrl> <!-- NO TRAILING SLASH -->
        <organization>organizationslug</organization>
        <authToken>APIauthToken</authToken>
        <repository>My Group / My Repository</repository>
        <projects>
            <project>projectslug1</project>
            <project>projectslug2</project>
        </projects>
    </configuration>
</plugin>
```

### Create or update release

Use the following command to create a release on the Sentry instance configured in the plugin
```bash
    mvn package sentry:release
```
### Deploy a release

To appear, a release must be deployed. Use the plugin `deploy` phase. An environment must be provided as parameter

```bash
    mvn sentry:deploy -Dsentry.environment=preproduction
```

You can also bind an environment by setting the property into your project pom.xml. The following example tells Maven to 
set the Sentry environment from a environment variable, which can be defined into your CI pipeline for example.

```xml
    <properties>
        <sentry.environment>${env.ENV_NAME}</sentry.environment>
    </properties>
```

## Advanced configuration

To configure the plugin correctly, you must follow the steps bellow

### Create an API Token

Firstly, you must create an API authentication token which allow the plugin to create and deploy a new release on Sentry. 
Login in your Sentry instance, then go to your account settings, in the "Auth Tokens" section.

Create a new token. Assign the project releases rights.

### (Optional) Enable the repository integration

If available, you can set a repository integration (with GitHub or GitLab) in the Sentry instance. 
When it is done, create the repositories on which Sentry will listen to commits. 

### Add the plugin to Maven

In your build section, add the plugin with the configuration.

```xml
<plugin>
    <groupId>com.jlefebure</groupId>
    <artifactId>sentry-release-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <configuration>
        <sentryUrl>https://yoursentryinstance.com/api/0</sentryUrl> <!-- NO TRAILING SLASH -->
        <organization>organizationslug</organization>
        <authToken>APIauthToken</authToken>
        <repository>My Group / My Repository</repository>
        <sendCommits>false</sendCommits>
        <projects>
            <project>projectslug1</project>
            <project>projectslug2</project>
        </projects>
    </configuration>
</plugin>
```

Parameters are the following

#### sentryUrl
Sentry URL, which can be a SAAS or a self-hosted instance. 

*This parameter is mandatory*

#### organization
The organization slug.

*This parameter is mandatory*

#### authToken
API Authentication token which allow to create or deploy a release.

*This parameter is mandatory*

#### repository
The optional repository name.
If an SCM integration has been set, then this field must be the name of the repository as defined in the Sentry instance.
Then, commits information are automatically provided by the integration plugin in Sentry and are not sent by the plugin.

If there is no repository integration, all commits information are sent to Sentry. Then, this field is optional.

#### sendCommits
Optionally force the plugin to send the commit information, event if a repository integration has been set. 

If not provided, the default value is false. If the `repository` parameter has not been provided, this parameter 
is ignored.


#### projects

List of all projects on which a release will be created. 

Must at least contain one project. 


### (Optional) Bind Maven phases

You can bind Maven phases with this plugin, to be able to create and deploy a release while the `deploy` Maven phase.

```xml
 <plugin>
     <groupId>com.jlefebure</groupId>
     <artifactId>sentry-release-maven-plugin</artifactId>
    <version>9.9.3.3</version>
     
     <configuration>
         <sentryUrl>https://yoursentryinstance.com/api/0</sentryUrl> <!-- NO TRAILING SLASH -->
         <organization>organizationslug</organization>
         <authToken>APIauthToken</authToken>
         <repository>My Group / My Repository</repository>
         <sendCommits>false</sendCommits>
         <projects>
             <project>projectslug1</project>
             <project>projectslug2</project>
         </projects>
         <executions>
            <execution>
              <id>release</id>
              <phase>package</phase>
              <goals>
                <goal>release</goal>
              </goals>
            </execution>
            <execution>
              <id>deploy</id>
              <phase>deploy</phase>
              <goals>
                <goal>deploy</goal>
              </goals>
            </execution>
          </executions>
     </configuration>
 </plugin>
 ```

Then, a release and a deploy are done when the following Maven command is executed.

```bash
    mvn clean package deploy
```
