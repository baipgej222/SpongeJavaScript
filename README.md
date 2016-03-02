# SpongeJavaScript v0.1
SpongeJavaScript is a plugin that allow anyone who know JavaScript to create their own plugins. The project is under the [MIT license](https://github.com/djxy/SpongeJavaScript/blob/master/License.md).

How SpongeJavaScript works
---
It use Nashorn, a JavaScript engine in Java. This engine give the possibility to the developers to code in JavaScript to interact with the Java objects. To do this, you have to do something like this.
```javascript
var player;

player.setDisplayName("Djxy");
```
With SpongeJavaScript you have the possibility to do the same thing, but with the JavaScript syntax.
```javascript
var player;

player.displayName = "Djxy";
```
The power of this plugin is that you can interact with any Java objects that are wrapped by the plugin. That mean you can interact with the **Sponge API** in JavaScript.
