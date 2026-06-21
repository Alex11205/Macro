import Link from "next/link";
import styles from "@/styles/Footer.module.css";

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.inner}>
        <div className={styles.brand}>
          <h2 className={styles.logo}>MacroTracker</h2>
          <p className={styles.tagline}>Discover destinations. Plan your next trip.</p>

          <div className={styles.socials}>
            <a href="#" aria-label="Facebook">fb</a>
            <a href="#" aria-label="Instagram">ig</a>
            <a href="#" aria-label="YouTube">yt</a>
          </div>
        </div>

        <div className={styles.linksColumn}>
          <h3>Explore</h3>
          <ul>
            <li><Link href="/">Home</Link></li>
            <li><Link href="/destination">Destinations</Link></li>
            <li><Link href="/trips">Trips</Link></li>
          </ul>
        </div>

        <div className={styles.linksColumn}>
          <h3>About Us</h3>
          <ul>
            <li><Link href="/contact">Contact</Link></li>
            <li><Link href="/contact">Our Team</Link></li>
            <li><Link href="/contact">Project Info</Link></li>
          </ul>
        </div>
      </div>

      <div className={styles.bottom}>
        <p>© 2026 MacroTracker</p>
      </div>
    </footer>
  );
}