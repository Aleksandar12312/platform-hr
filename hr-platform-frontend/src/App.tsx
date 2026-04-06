import { useState } from 'react';
import { api } from './api';
import type { CandidateDto } from './dto/CandidateDto';
import { CandidateCard } from './CandidateCard';

function App() {
  const [candidates, setCandidates] = useState<CandidateDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [searchName, setSearchName] = useState('');
  const [searchSkill, setSearchSkill] = useState('');


  const [newName, setNewName] = useState('');
  const [newEmail, setNewEmail] = useState('');
  const [newPhone, setNewPhone] = useState('');
  const [newBirthday, setNewBirthday] = useState('');

  const loadCandidates = async () => {
    try {
      setLoading(true);
      if (searchName) {
        const data = await api.searchByName(searchName);
        setCandidates(data);
      } else if (searchSkill) {
        const data = await api.searchBySkill(searchSkill);
        setCandidates(data);
      } else {
        const data = await api.getAllCandidates();
        setCandidates(data);
      }
      setError(null);
    } catch (err: any) {
      setError(err.message || 'Failed to fetch candidates');
    } finally {
      setLoading(false);
    }
  };

  const handleGetAllCandidates = async () => {
    try {
      setLoading(true);
      const data = await api.getAllCandidates();
      setCandidates(data);

      setSearchName('');
      setSearchSkill('');
      setError(null);
    } catch (err: any) {
      setError(err.message || 'Failed to load all candidates');
    } finally {
      setLoading(false);
    }
  };

  const handleSearchByName = async () => {
    if (!searchName.trim()) return;
    try {
      setLoading(true);
      const data = await api.searchByName(searchName);
      setCandidates(data);
      setSearchSkill('');
      setError(null);
    } catch (err: any) {
      setError(err.message || 'Failed to search by name');
    } finally {
      setLoading(false);
    }
  };

  const handleSearchBySkill = async () => {
    if (!searchSkill.trim()) return;
    try {
      setLoading(true);
      const data = await api.searchBySkill(searchSkill);
      setCandidates(data);
      setSearchName('');
      setError(null);
    } catch (err: any) {
      setError(err.message || 'Failed to search by skill');
    } finally {
      setLoading(false);
    }
  };

  const handleAddCandidate = async (e: React.SubmitEvent) => {
    e.preventDefault();
    if (!newName.trim()) return;
    try {
      await api.addCandidate({
        name: newName,
        email: newEmail,
        contactNumber: newPhone,
        birthday: newBirthday || undefined
      });
      setNewName('');
      setNewEmail('');
      setNewPhone('');
      setNewBirthday('');
      loadCandidates();
    } catch (err: any) {
      setError(err.message || 'Failed to add candidate');
    }
  };

  const handleDeleteCandidate = async (id: number) => {
    try {
      await api.removeCandidate(id);
      loadCandidates();
    } catch (err: any) {
      setError(err.message || 'Failed to delete candidate');
    }
  };

  const handleAddSkill = async (candidateId: number, skillName: string) => {
    try {
      await api.addSkill(candidateId, { skillName });
      loadCandidates();
    } catch (err: any) {
      setError(err.message || 'Failed to add skill');
    }
  };

  const handleRemoveSkill = async (candidateId: number, skillId: number) => {
    try {
      await api.removeSkill(candidateId, skillId);
      loadCandidates();
    } catch (err: any) {
      setError(err.message || 'Failed to remove skill');
    }
  };

  return (
    <div className="container">
      <h1 className="title">HR Platform</h1>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="dashboard">
        <aside>
          <div className="card">
            <h2 className="section-title">Add Candidate</h2>
            <form onSubmit={handleAddCandidate}>
              <div className="form-group">
                <label className="form-label">Full Name *</label>
                <input required type="text" className="input-field" value={newName}
                  onChange={e => setNewName(e.target.value)} placeholder="Joe Doe" />
              </div>
              <div className="form-group">
                <label className="form-label">Email</label>
                <input type="email" className="input-field" value={newEmail}
                  onChange={e => setNewEmail(e.target.value)} placeholder="joe@example.com" />
              </div>
              <div className="form-group">
                <label className="form-label">Phone</label>
                <input type="tel" className="input-field" value={newPhone}
                  onChange={e => setNewPhone(e.target.value)} placeholder="+381 164748123" />
              </div>
              <div className="form-group">
                <label className="form-label">Birthday</label>
                <input type="date" className="input-field" value={newBirthday}
                  onChange={e => setNewBirthday(e.target.value)} />
              </div>
              <button type="submit" className="btn btn-primary btn-block">
                Create Candidate
              </button>
            </form>
          </div>
        </aside>

        <main>
          <div className="search-bar">
            <div>
              <label className="form-label">Search by Name</label>
              <div className="flex-gap">
                <input
                  type="text"
                  className="input-field"
                  placeholder="Type name..."
                  value={searchName}
                  onChange={e => setSearchName(e.target.value)}
                />
                <button className="btn btn-primary" onClick={handleSearchByName}>Search</button>
              </div>
            </div>
            <br></br>
            <div>
              <label className="form-label">Search by Skill</label>
              <div className="flex-gap">
                <input
                  type="text"
                  className="input-field"
                  placeholder="Type skill..."
                  value={searchSkill}
                  onChange={e => setSearchSkill(e.target.value)}
                />
                <button className="btn btn-primary" onClick={handleSearchBySkill}>Search</button>
              </div>
            </div>
          </div>

          <div className="action-row">
            <button className="btn btn-primary" onClick={handleGetAllCandidates}>
              Load All Candidates
            </button>
          </div>

          <div className="candidate-grid">
            {candidates.length > 0 ? (
              candidates.map(candidate => (
                <CandidateCard
                  key={candidate.id}
                  candidate={candidate}
                  onDelete={handleDeleteCandidate}
                  onAddSkill={handleAddSkill}
                  onRemoveSkill={handleRemoveSkill}
                />
              ))
            ) : (
              <p className="empty-message">No candidates found.</p>
            )}
          </div>
        </main>
      </div>
    </div>
  );
}

export default App;