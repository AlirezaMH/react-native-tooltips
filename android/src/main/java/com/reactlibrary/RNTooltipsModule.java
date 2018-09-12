
package com.reactlibrary;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.views.view.ReactViewGroup;
import com.github.florent37.viewtooltip.ViewTooltip;

public class RNTooltipsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private ViewTooltip tooltip;

  public RNTooltipsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNTooltips";
  }

  @ReactMethod
  public void Show(final int view, final ReadableMap props, final Callback onHide) {
    final Activity activity = this.getCurrentActivity();
    final ViewGroup target = activity.findViewById(view);

    if (target == null) {
        // The reference to the component haven't been rendered on the js side yet.
        // View id doesn't exist on the native side yet.
        return;
    }

    String text = props.getString("text");
    int position = props.getInt("position");
    int align = props.getInt("align");
    boolean autoHide = props.getBoolean("autoHide");
    int duration = props.getInt("duration");
    boolean clickToHide = props.getBoolean("clickToHide");
    int corner = props.getInt("corner");
    String tintColor = props.getString("tintColor");
    String textColor = props.getString("textColor");
    int textSize = props.getInt("textSize");
    int gravity = props.getInt("gravity");
    boolean arrow = props.getBoolean("arrow");

    tooltip = ViewTooltip.on(reactContext.getCurrentActivity(), target);
    tooltip = tooltip.text(text);

    if (!arrow) {
      tooltip.arrowHeight(0);
      tooltip.arrowWidth(0);
    }

    if (position == 1) {
      tooltip = tooltip.position(ViewTooltip.Position.LEFT);
    } else if (position == 2) {
      tooltip = tooltip.position(ViewTooltip.Position.RIGHT);
    } else if (position == 3) {
      tooltip = tooltip.position(ViewTooltip.Position.TOP);
    } else if (position == 4) {
      tooltip = tooltip.position(ViewTooltip.Position.BOTTOM);
    }

    if (align == 1) {
      tooltip = tooltip.align(ViewTooltip.ALIGN.START);
    } else if (align == 2) {
      tooltip = tooltip.align(ViewTooltip.ALIGN.CENTER);
    } else if (align == 3) {
      tooltip = tooltip.align(ViewTooltip.ALIGN.END);
    }

    tooltip = tooltip.autoHide(autoHide, duration);
    tooltip = tooltip.clickToHide(clickToHide);
    tooltip = tooltip.corner(corner);
    tooltip = tooltip.color(Color.parseColor(tintColor));
    tooltip = tooltip.textColor(Color.parseColor(textColor));
    tooltip = tooltip.textSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    tooltip = tooltip.setTextGravity(gravity);

    tooltip.onHide(new ViewTooltip.ListenerHide() {
      @Override
      public void onHide(View view) {
        onHide.invoke();
      }
    });

    tooltip.show();
  }

  @ReactMethod
  public void Dismiss(final int view) {

    if (tooltip == null) return;

    tooltip.close();
    tooltip = null;
  }
}
