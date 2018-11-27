readme
===
# 一、前言
登录页面涉及到密码的输入时，一般都会使用框架提供的自绘键盘。然而框架键盘封装的并不是很友好，使用起来较为麻烦，而且有些特性没有进行支持。比如没有实现键盘弹出时，把焦点视图往上顶使其不被遮住。再比如框架不支持自定义键盘的UI（虽然框架内置了很多键盘类型，但是需求这玩意不好说，个人建议还是要支持一下比较好）。

综上，本人基于框架的KeyboardManager，二次封装实现了FlexKeyboardManager，解决了上述痛点。

# 二、使用对比
先看下框架键盘的使用，首先需要针对页面里的每个EditText实例化一个KeyboardManager对象，然后使用这个对象操作键盘的显示和隐藏。需要注意显示之前要把另一个已经显示的框架键盘关闭掉。

```
KeyboardManager keyboardManager = new KeyboardManager(mActivity, mEdtPassword,
                KeyboardManager.KEYBOARD_TYPE_IOS_DIGITAL_RANDOM);
keyboardManager.show();
keyboardManager.dismiss();
```
对于键盘的弹出和隐藏需要监听EditText的焦点变化来决定，因为框架键盘绑定了具体的EditText，所以遇到复杂的业务场景时会比较蛋疼。其实，键盘的弹出不应该与EditText进行绑定的，只需要把window里获得焦点的视图（通常是EditText）作为输入的目标就行了。系统键盘的弹出就是这样处理的，并且系统键盘跑在输入法进程，解耦的还更彻底。

再来看下本人二次封装的FlexKeyboardManager，只需使用如下类
```
public class KeyboardEditText extends AppCompatEditText{}
```
代替系统的EditText即可。KeyboardEditText扩展了系统的EditText，仅仅是默认弹出自绘键盘，其余跟系统的EditText一毛一样。

KeyboardEditText提供了以下几个属性，实现更精细化的键盘控制。
1. keyboard_type：注意这个不是框架那个KeyboardType。这个属性用于控制获取焦点时弹的是自绘键盘还是系统键盘。
2. keyboard_theme_id：这个属性用于指定自绘键盘的UI样式，可以填自定义的，也可以使用内置的样式。比如想使用框架提供的ios数字随机键盘，可以这样配置：

```
<com.hua.keyboardmanager_core.KeyboardEditText
        android:layout_width="match_parent"
        android:layout_height="60dp"
        app:keyboard_theme_id="@id/tk_keyboard_theme_ios_digital_random" />
```
3. keyboard_visible_view：这个属性用于指定键盘弹出时，想保持哪个视图不被键盘遮挡。如果不指定，则保证当前获取焦点的EditText不被遮挡。比如配置页面中的登录按钮不被遮挡，可以这样配置：

```
<com.hua.keyboardmanager_core.KeyboardEditText
        android:layout_width="match_parent"
        android:layout_height="60dp"
        app:keyboard_visible_view="@+id/btn_login" />
```
关于自定义UI，只需实现以下接口

```
public interface IKeyboardTheme {}
```
然后，注册到FlexKeyboardManager中即可


```
public static void registerKeyboardTheme(IKeyboardTheme theme) {}
```
# 三、效果展示

本想录gif，但是太麻烦，而且看不出个所以然。所以大家直接下载打包好的demo apk看吧。[戳我下载](http://192.168.90.155/hua/FlexKeyboardManager/blob/master/keyboard_demo.apk)



















