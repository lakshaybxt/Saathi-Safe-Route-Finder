import { FaUsers, FaMapMarkedAlt } from 'react-icons/fa';
import { motion } from 'framer-motion';
import './trust.css';

function Trust () {
	return (
		<section className="trust-section">
			<motion.div 
				className="stat-card"
				initial={{ opacity: 0, y: 30 }}
				whileInView={{ opacity: 1, y: 0 }}
				transition={{ duration: 0.6, ease: "easeOut" }}
				viewport={{ once: true }}
			>
				<FaUsers className="stat-icon"/>
				<h3>1,000+ Reviewers</h3>
				<p>Real experiences from real people</p>
			</motion.div>
			<motion.div 
				className="stat-card"
				initial={{ opacity: 0, y: 30 }}
				whileInView={{ opacity: 1, y: 0 }}
				transition={{ duration: 0.6, ease: "easeOut", delay: 0.2 }}
				viewport={{ once: true }}
			>
				<FaMapMarkedAlt className="stat-icon"/>
				<h3>50+ Cities Covered</h3>
				<p>More cities joining soon</p>
			</motion.div>
		</section>
	);
}

export default Trust