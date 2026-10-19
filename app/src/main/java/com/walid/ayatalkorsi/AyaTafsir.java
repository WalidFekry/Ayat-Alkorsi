package com.walid.ayatalkorsi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class AyaTafsir extends AppCompatActivity {

    private static final String WORDS_MEANING =
            "• (اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ): لا معبود بحق في الوجود إلا الله وحده لا شريك له.\n\n" +
            "• (الْحَيُّ): الحي الحياة الكاملة الدائمة بلا بداية ولا نهاية.\n\n" +
            "• (الْقَيُّومُ): القائم بنفسه، القائم على تدبير خلق السماوات والأرض.\n\n" +
            "• (سِنَةٌ): النعاس الذي هو مقدمة النوم.\n\n" +
            "• (لا يَئُودُهُ): لا يثقله ولا يعجزه ولا يجهده حفظ السماوات والأرض.\n\n" +
            "• (وَسِعَ كُرْسِيُّهُ): الكرسي موضع القدمين وهو مخلوق عظيم أوسع من السماوات والأرض.\n\n" +
            "• (الْعَلِيُّ الْعَظِيمُ): ذو العلو المطلق بذاته وقدره وقحره، العظيم في ملكه وسلطانه.";

    private static final String TAFSIR_DETAIL =
            "الله جل جلاله هو الأحد الفرد الصمد الذي لا يستحق العبادة سواه. " +
            "هو الحي القيوم الذي له الحياة الكاملة، والقائم على حفظ جميع المخلوقات وتدبير شؤونها. " +
            "لا ينبغي له ولا يعتريه نعاس ولا نوم لكمال حياته وقيوميته.\n\n" +
            "له ملك السماوات والأرض بأسرهما، فلا يخرج شيء عن سلطانه. " +
            "ولا يستطيع أحد أن يشفع عنده إلا بإذنه ورضاه كرامة للشافع.\n\n" +
            "يعلم ما كان وما يكون وما هو كائن، أحاط علمه بجميع الكائنات ظاهرة وباطنة. " +
            "ولا يحيط العباد بشيء من علمه إلا بما يسر لهم معرفته.\n\n" +
            "وسع كرسيه السماوات والأرض سعة وعظمة، ولا يعجزه ولا يثقله حفظهما ورعايتهما، وهو العلي بذاته العظيم بقدره وشأنه.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aya_tafsir);

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        TextView wordsMeaningTv = findViewById(R.id.words_meaning_text);
        TextView tafsirDetailTv = findViewById(R.id.tafsir_detail_text);

        wordsMeaningTv.setText(WORDS_MEANING);
        tafsirDetailTv.setText(TAFSIR_DETAIL);

        Button btnCopy = findViewById(R.id.btn_copy_tafsir);
        Button btnShare = findViewById(R.id.btn_share_tafsir);

        btnCopy.setOnClickListener(v -> copyTafsirToClipboard());
        btnShare.setOnClickListener(v -> shareTafsir());
    }

    private void copyTafsirToClipboard() {
        String fullText = "تفسير آية الكرسي ومعاني كلماتها:\n\n" + WORDS_MEANING + "\n\n" + TAFSIR_DETAIL;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("تفسير آية الكرسي", fullText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
        }
    }

    private void shareTafsir() {
        String fullText = "تفسير آية الكرسي ومعاني كلماتها:\n\n" + WORDS_MEANING + "\n\n" + TAFSIR_DETAIL + "\n\nمن تطبيق آية الكرسي مكررة بدون إنترنت";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "تفسير آية الكرسي");
        shareIntent.putExtra(Intent.EXTRA_TEXT, fullText);
        startActivity(Intent.createChooser(shareIntent, "مشاركة التفسير"));
    }
}
