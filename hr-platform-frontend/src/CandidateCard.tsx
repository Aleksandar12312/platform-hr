import { useState } from 'react';
import type { CandidateDto } from './dto/CandidateDto';

interface Props {
  candidate: CandidateDto;
  onDelete: (id: number) => void;
  onAddSkill: (candidateId: number, skillName: string) => void;
  onRemoveSkill: (candidateId: number, skillId: number) => void;
}

export function CandidateCard({ candidate, onDelete, onAddSkill, onRemoveSkill }: Props) {
  const [newSkillName, setNewSkillName] = useState('');
  const [selectedSkillId, setSelectedSkillId] = useState<string>('');

  const handleAddSkill = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newSkillName.trim() || !candidate.id) return;
    onAddSkill(candidate.id, newSkillName.trim());
    setNewSkillName('');
  };

  return (
    <div className="card">
      <div className="candidate-header">
        <div className="candidate-name">{candidate.name}</div>
        <button className="btn btn-danger btn-sm" onClick={() => candidate.id && onDelete(candidate.id)}>Delete</button>
      </div>
      <div className="candidate-info">
        <p><strong>Email:</strong> {candidate.email || 'N/A'}</p>
        <p><strong>Phone:</strong> {candidate.contactNumber || 'N/A'}</p>
        <p><strong>Birthday:</strong> {candidate.birthday || 'N/A'}</p>
      </div>

      <div className="skills-section" style={{ marginTop: '1rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', marginBottom: '0.5rem', gap: '0.5rem' }}>
          <label style={{ fontWeight: 600 }}>Skills:</label>
          <select 
            className="input-field" 
            style={{ margin: 0, flex: 1, padding: '0.3rem 0.5rem' }}
            value={selectedSkillId}
            onChange={(e) => setSelectedSkillId(e.target.value)}
          >
            <option value="" disabled>
              {candidate.skills && candidate.skills.length > 0 ? 'Select a skill...' : 'No skills added yet'}
            </option>
            {candidate.skills?.map(skill => (
              <option key={skill.id} value={skill.id}>
                {skill.skillName}
              </option>
            ))}
          </select>
          {selectedSkillId && (
            <button 
              className="btn btn-danger btn-sm"
              onClick={() => {
                if (candidate.id) {
                  onRemoveSkill(candidate.id, Number(selectedSkillId));
                  setSelectedSkillId('');
                }
              }}
            >
              Remove
            </button>
          )}
        </div>
        
        <form className="add-skill-form" onSubmit={handleAddSkill} style={{ display: 'flex', gap: '0.5rem' }}>
          <input 
            type="text" 
            className="input-field" 
            style={{ flex: 1, margin: 0 }}
            placeholder="Add new skill..." 
            value={newSkillName}
            onChange={(e) => setNewSkillName(e.target.value)}
          />
          <button type="submit" className="btn btn-primary btn-sm" style={{padding: '0.3rem 0.75rem'}}>Add</button>
        </form>
      </div>
    </div>
  );
}
