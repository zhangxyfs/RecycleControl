package com.z7dream.android_recycler_control;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.z7dream.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * �հ�ҳ����
 * Created by Z7Dream on 2017/8/16 14:16.
 * Email:zhangxyfs@126.com
 */
public class EmptyViewControl<VIEW extends ViewGroup> {
    private ViewGroup parent;
    private ViewGroup outRecyclerLayout;
    private RelativeLayout childLayout;
    private TextView childTextView;

    private Drawable errorEmptyDrawable, errorNetDrawable;
    private String emptyTextStr, netTextStr;
    private ErrorClickListener errorClickListener;

    /**
     * ��ʼ��
     *
     * @param outRecyclerLayout recyclerView����listview���layout
     */
    public EmptyViewControl(VIEW outRecyclerLayout) {
        init(outRecyclerLayout);
    }

    /**
     * ��ʼ��
     *
     * @param layout recyclerView����listview���layout
     */
    private void init(VIEW layout) {
        if (parent != null) return;
        parent = (ViewGroup) layout.getParent();
        outRecyclerLayout = layout;

        childLayout = new RelativeLayout(parent.getContext());
        childLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        childTextView = new TextView(parent.getContext());
        RelativeLayout.LayoutParams textLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textLP.addRule(RelativeLayout.CENTER_IN_PARENT);
        childTextView.setLayoutParams(textLP);

        childLayout.addView(childTextView);
        parent.addView(childLayout);

        childTextView.setTextColor(Color.BLACK);
        childTextView.setTextSize(COMPLEX_UNIT_SP, 16);
        childTextView.setGravity(Gravity.CENTER);

        errorEmptyDrawable = parent.getResources().getDrawable(R.drawable.ic_error_empty);
        errorNetDrawable = parent.getResources().getDrawable(R.drawable.ic_error_net);
        errorEmptyDrawable.setBounds(0, 0, errorEmptyDrawable.getIntrinsicWidth(), errorEmptyDrawable.getIntrinsicHeight());
        errorNetDrawable.setBounds(0, 0, errorNetDrawable.getIntrinsicWidth(), errorNetDrawable.getIntrinsicHeight());

        emptyTextStr = parent.getResources().getString(R.string.error_empty_str);
        netTextStr = parent.getResources().getString(R.string.error_net_str);

        childLayout.setVisibility(View.GONE);
        childLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorClickListener != null && childLayout.getVisibility() == View.VISIBLE) {
                    errorClickListener.emptyClickListener();
                    unDisplayError();
                }

            }
        });
    }

    /**
     * ����������
     *
     * @param errorClickListener �������
     */
    public void setOnErrorClickListener(ErrorClickListener errorClickListener) {
        this.errorClickListener = errorClickListener;
    }

    /**
     * �����ı���Ϣ
     *
     * @param color ��ɫ
     * @param size  �ֺ�
     */
    public void setTextViewInfo(int color, float size) {
        if (childTextView == null) return;
        if (color > 0)
            childTextView.setTextColor(color);

        if (size > 0)
            childTextView.setTextSize(size);
    }

    /**
     * �����ı���Ϣ
     *
     * @param color ��ɫ
     * @param sp    �ֺ�
     */
    public void setTextViewInfo(int color, int sp) {
        if (childTextView == null) return;
        if (color > 0)
            childTextView.setTextColor(color);

        if (sp > 0)
            childTextView.setTextSize(COMPLEX_UNIT_SP, sp);
    }

    /**
     * adapter������ʱ��
     *
     * @param content ��������
     */
    public void setErrorEmptyStr(String content) {
        if (!TextUtils.isEmpty(content)) {
            emptyTextStr = content;
        }
    }

    /**
     * adapter��������ʱ��
     *
     * @param content ��������
     */
    public void setErrorNetStr(String content) {
        if (!TextUtils.isEmpty(content)) {
            netTextStr = content;
        }
    }

    /**
     * ����������ʱ���ͼƬ��Դ
     *
     * @param resId ͼƬ��Դid
     */
    public void setErrorEmptyDrawable(int resId) {
        if (errorEmptyDrawable == null || parent == null) return;
        errorEmptyDrawable = parent.getResources().getDrawable(resId);
        errorEmptyDrawable.setBounds(0, 0, errorEmptyDrawable.getIntrinsicWidth(), errorEmptyDrawable.getIntrinsicHeight());
    }

    /**
     * ����������ʱ���ͼƬ��Դ
     *
     * @param drawable ͼƬdrawable
     */
    public void setErrorEmptyDrawable(Drawable drawable) {
        if (errorEmptyDrawable == null) return;
        errorEmptyDrawable = drawable;
        errorEmptyDrawable.setBounds(0, 0, errorEmptyDrawable.getIntrinsicWidth(), errorEmptyDrawable.getIntrinsicHeight());
    }

    /**
     * �����������ʱ���ͼƬ��Դ
     *
     * @param resId ͼƬ��Դid
     */
    public void setErrorNetDrawable(int resId) {
        if (errorNetDrawable == null || parent == null) return;
        errorNetDrawable = parent.getResources().getDrawable(resId);
        errorNetDrawable.setBounds(0, 0, errorNetDrawable.getIntrinsicWidth(), errorNetDrawable.getIntrinsicHeight());
    }

    /**
     * �����������ʱ���ͼƬ��Դ
     *
     * @param drawable ͼƬdrawable
     */
    public void setErrorNetDrawable(Drawable drawable) {
        if (errorNetDrawable == null) return;
        errorNetDrawable = drawable;
        errorNetDrawable.setBounds(0, 0, errorNetDrawable.getIntrinsicWidth(), errorNetDrawable.getIntrinsicHeight());
    }

    /**
     * ��ʾ������
     */
    public void displayErrorEmpty() {
        if (childLayout == null || outRecyclerLayout == null || childTextView == null || errorEmptyDrawable == null)
            return;
        childLayout.setVisibility(View.VISIBLE);
        outRecyclerLayout.setVisibility(View.GONE);
        childTextView.setText(emptyTextStr);
        childTextView.setCompoundDrawables(null, errorEmptyDrawable, null, null);
        childTextView.setCompoundDrawablePadding(convertDipOrPx(8));
    }

    /**
     * ��ʾ�������
     */
    public void displayErrorNet() {
        if (childLayout == null || outRecyclerLayout == null || childTextView == null || errorEmptyDrawable == null)
            return;
        childLayout.setVisibility(View.VISIBLE);
        outRecyclerLayout.setVisibility(View.GONE);
        childTextView.setText(netTextStr);
        childTextView.setCompoundDrawables(null, errorNetDrawable, null, null);
        childTextView.setCompoundDrawablePadding(convertDipOrPx(8));
    }

    /**
     * ȡ����ʾ����ҳ��
     */
    public void unDisplayError() {
        if (childLayout == null || outRecyclerLayout == null) return;
        childLayout.setVisibility(View.GONE);
        outRecyclerLayout.setVisibility(View.VISIBLE);
    }

    /**
     * ����
     */
    public void destory() {
        parent = null;
        outRecyclerLayout = null;
        childLayout = null;
        childTextView = null;

        errorEmptyDrawable = null;
        errorNetDrawable = null;
        errorClickListener = null;
    }

    public interface ErrorClickListener {
        void emptyClickListener();
    }

    private int convertDipOrPx(float dip) {
        float scale = parent.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
