import React, { useState } from 'react';
import submit from '../../assets/submit.svg'
import './place.css'
import './about.css'

function Place () {
	const [rating, setRating] = useState(0);
	const [hover, setHover] = useState(0);
	const [currentStep, setCurrentStep] = useState(1);

	const handleNextStep = () => {
		let report = document.querySelector('.testimonial-form');
		let place = document.querySelector('.place-form');
		report.classList.toggle('change');
		place.classList.toggle('change');
		setCurrentStep(currentStep === 1 ? 2 : 1);
	}

	return (
		<div className='page-container'>
			

			<div className='main-class'>
				
				<section className="content">
					{/* Step Indicator */}
			<div className='steps-indicator'>
				<div className="steps-container">
					<div className={`step ${currentStep === 1 ? 'active' : 'completed'}`}>
						<div className='step-number'>1</div>
					</div>
					<div className='step-line'></div>
					<div className={`step ${currentStep === 2 ? 'active' : ''}`}>
						<div className='step-number'>2</div>
					</div>
				</div>
			</div>
					<div className="parent">
						<div className="child-shadow"></div>
						<div className="child">
							Want to share your experience here? <br/>
							Leave a short testimonial.
						</div>
					</div>
				</section>
				
				<div className='form'>
					{/* User Place section */}
					<div className='place-form'>
						<h2>Enter Place Details</h2>

						<div className="form-row">
							<div className="form-group"> 
								<span className="floating-label">Locality</span>
								<input className="input-holder"></input>
							</div>
							<div className="form-group"> 
								<span className="floating-label">City</span>
								<input className="input-holder"></input>
							</div>
						</div>

						<div className="form-row">
							<div className="form-group"> 
								<span className="floating-label">State</span>
								<input className="input-holder"></input>
							</div>
							<div className="form-group"> 
								<span className="floating-label">Postal Code</span>
								<input className="input-holder"></input>
							</div>
						</div>

						<div className="form-row">
							<div className="form-group"> 
								<span className="floating-label">Country</span>
								<input className="input-holder"></input>
							</div>
							<button 
								className="submit-button"
								onClick={handleNextStep}>
								<img src={submit}/>
								</button>
						</div>
					</div>
					{/* User Report Area Section */}
					<div className='testimonial-form change'>
						<h2>Share Your Experience about <br/> <span>Delhi</span></h2>

						<div className="report-row">
							<div className=" rating-box"> 
								<span className="floating-label">Rate this area</span>
								<div className='stars'>
									{[1, 2, 3, 4, 5].map((star) => {
										return <span
											key={star}
											className={`star ${star <= (hover || rating) ? 'filled' : ''}`}
											onClick={() => setRating(star)}
											onMouseEnter={() => setHover(star)}
											onMouseLeave={() => setHover(0)}
										>★</span>
									})}
								</div>
							</div>
							<div className="form-group"> 
								<span className="floating-label">Review</span>
								<textarea className="text-holder"></textarea>
							</div>
							<div className="form-group"> 
								<span className="floating-label">Tip to follow?</span>
								<textarea className="text-holder"></textarea>
							</div>
						</div>
						<div className='button-section'>
							<button className='send-button'>
								<svg 
										className="w-6 h-6" 
										fill="none" 
										stroke="#52B2E5" 
										viewBox="0 0 24 24"
									>
										<path 
											strokeLinecap="round" 
											strokeLinejoin="round" 
											strokeWidth={2} 
											d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"
										/>
									</svg>
							</button>
						</div>

					</div> 
				</div>
			</div>

		</div>
	);
}

export default Place