import Link from "next/link";
import styles from "@/styles/Footer.module.css";

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.inner}>
        <div className={styles.brand}>
          <h2 className={styles.logo}>MacroTracker</h2>
          <p className={styles.tagline}>Track calories and macronutrients in your food</p>
        </div>

      </div>

      <div className={styles.bottom}>
        <p>© 2026 MacroTracker</p>
      </div>
    </footer>
  );
}